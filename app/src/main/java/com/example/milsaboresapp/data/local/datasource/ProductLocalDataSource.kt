package com.example.milsaboresapp.data.local.datasource

import com.example.milsaboresapp.data.local.dao.ProductDao
import com.example.milsaboresapp.data.local.entity.ProductoEntity
import com.example.milsaboresapp.data.local.datasource.ProductSeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * DataSource centralizado que mantiene sincronizado el catálogo de productos
 * usando Room como fuente de verdad. Se encarga de sembrar la tabla al iniciar
 * y expone un Flow reactivo con la lista ordenada.
 */
class ProductLocalDataSource(
    private val productDao: ProductDao,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) {

    private val seedsById = ProductSeedData.productos.associateBy { it.id }
    private val seedsByName = ProductSeedData.productos.associateBy { normalizeName(it.nombre) }

    private val productsState = MutableStateFlow<List<ProductoEntity>>(emptyList())

    init {
        scope.launch {
            seedIfNeeded()
            productDao.observeAll().collectLatest { entities ->
                productsState.value = entities
            }
        }
    }

    fun observeProducts(): Flow<List<ProductoEntity>> = productsState.asStateFlow()

    suspend fun upsert(entity: ProductoEntity) {
        productDao.insert(mergeEntity(entity))
    }

    suspend fun upsertAll(entities: List<ProductoEntity>) {
        if (entities.isEmpty()) return
        val currentList = productDao.getAll()
        val currentById = currentList.associateBy { it.id }
        val currentByName = currentList.associateBy { normalizeName(it.nombre) }
        val merged = entities.map { entity ->
            val normalizedName = normalizeName(entity.nombre)
            val existing = currentById[entity.id] ?: currentByName[normalizedName]
            val targetId = existing?.id ?: entity.id
            val seed = seedsById[targetId] ?: seedsByName[normalizedName]
            mergeEntity(
                incoming = entity.copy(id = targetId),
                existing = existing,
                seed = seed
            )
        }
        productDao.insertAll(merged)
    }

    suspend fun update(entity: ProductoEntity) {
        productDao.update(entity)
    }

    suspend fun delete(id: String) {
        productDao.deleteById(id)
    }

    suspend fun getById(id: String): ProductoEntity? = productDao.getById(id)

    private suspend fun seedIfNeeded() {
        val current = productDao.getAll()
        if (current.isEmpty()) {
            productDao.insertAll(ProductSeedData.productos)
            return
        }

        restoreSeedMetadata(current)
    }

    private suspend fun restoreSeedMetadata(current: List<ProductoEntity>) {
        if (current.isEmpty()) return

        val seedsById = ProductSeedData.productos.associateBy { it.id }

        val updates = current.mapNotNull { entity ->
            val seed = seedsById[entity.id] ?: return@mapNotNull null

            val needsAttr = entity.attr.isBlank() && seed.attr.isNotBlank()
            val needsImg = entity.img.isBlank() && seed.img.isNotBlank()
            val needsDescription = entity.descripcion.isBlank() && seed.descripcion.isNotBlank()
            val needsStockCritico = entity.stockCritico == 0 && seed.stockCritico > 0

            if (!(needsAttr || needsImg || needsDescription || needsStockCritico)) {
                return@mapNotNull null
            }

            entity.copy(
                attr = if (needsAttr) seed.attr else entity.attr,
                img = if (needsImg) seed.img else entity.img,
                descripcion = if (needsDescription) seed.descripcion else entity.descripcion,
                stockCritico = if (needsStockCritico) seed.stockCritico else entity.stockCritico
            )
        }

        if (updates.isNotEmpty()) {
            productDao.insertAll(updates)
        }

        val existingIds = current.map { it.id }.toSet()
        val missingSeeds = seedsById.filterKeys { it !in existingIds }.values
        if (missingSeeds.isNotEmpty()) {
            productDao.insertAll(missingSeeds.toList())
        }

        removeDuplicateNames()
    }

    private suspend fun mergeEntity(incoming: ProductoEntity): ProductoEntity {
        val normalizedName = normalizeName(incoming.nombre)
        val existing = productDao.getById(incoming.id) ?: productDao.findByName(incoming.nombre)
        val targetId = existing?.id ?: incoming.id
        val seed = seedsById[targetId] ?: seedsByName[normalizedName]
        return mergeEntity(
            incoming = incoming.copy(id = targetId),
            existing = existing,
            seed = seed
        )
    }

    private fun mergeEntity(
        incoming: ProductoEntity,
        existing: ProductoEntity?,
        seed: ProductoEntity?
    ): ProductoEntity {

        fun String?.fallback(vararg options: String): String {
            val primary = this?.takeIf { it.isNotBlank() }
            if (primary != null) return primary
            return options.firstOrNull { it.isNotBlank() } ?: ""
        }

        val nombre = incoming.nombre.fallback(existing?.nombre ?: "", seed?.nombre ?: incoming.nombre)
        val categoria = incoming.categoria.fallback(existing?.categoria ?: "", seed?.categoria ?: "")
        val attr = incoming.attr.fallback(existing?.attr ?: "", seed?.attr ?: "")
        val img = incoming.img.fallback(existing?.img ?: "", seed?.img ?: "")
        val descripcion = incoming.descripcion.fallback(existing?.descripcion ?: "", seed?.descripcion ?: "")

        val precio = when {
            incoming.precio > 0 -> incoming.precio
            existing?.precio?.takeIf { it > 0 } != null -> existing.precio
            seed?.precio?.takeIf { it > 0 } != null -> seed.precio
            else -> 0
        }

        val stockCritico = when {
            incoming.stockCritico > 0 -> incoming.stockCritico
            existing?.stockCritico?.takeIf { it > 0 } != null -> existing.stockCritico
            seed?.stockCritico?.takeIf { it > 0 } != null -> seed.stockCritico
            else -> 0
        }

        val stock = incoming.stock

        return ProductoEntity(
            id = incoming.id,
            nombre = nombre,
            precio = precio,
            categoria = categoria,
            attr = attr,
            img = img,
            stock = stock,
            stockCritico = stockCritico,
            descripcion = descripcion
        )
    }

    private suspend fun removeDuplicateNames() {
        val all = productDao.getAll()
        val grouped = all.groupBy { normalizeName(it.nombre) }
        grouped.values.forEach { entries ->
            if (entries.size <= 1) return@forEach

            val preferred = entries.maxByOrNull { entity ->
                when {
                    seedsById.containsKey(entity.id) -> 2
                    else -> 1
                }
            } ?: return@forEach

            entries.filter { it.id != preferred.id }.forEach { duplicate ->
                productDao.deleteById(duplicate.id)
            }
        }
    }

    private fun normalizeName(value: String): String = value.trim().lowercase()
}
