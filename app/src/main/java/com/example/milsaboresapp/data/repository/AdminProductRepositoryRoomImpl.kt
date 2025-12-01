package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.AdminSeedData
import com.example.milsaboresapp.data.local.datasource.ProductLocalDataSource
import com.example.milsaboresapp.data.local.datasource.ProductSeedData
import com.example.milsaboresapp.data.local.entity.ProductoEntity
import com.example.milsaboresapp.domain.model.admin.AdminProductFormContent
import com.example.milsaboresapp.domain.model.admin.AdminProductItem
import com.example.milsaboresapp.domain.repository.AdminProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class AdminProductRepositoryRoomImpl(
    private val productLocalDataSource: ProductLocalDataSource
) : AdminProductRepository {

    private val formState = MutableStateFlow(AdminSeedData.productFormContent)

    override fun observeProducts(): Flow<List<AdminProductItem>> =
        productLocalDataSource.observeProducts().map { entities ->
            entities.map { entity ->
                AdminProductItem(
                    code = entity.id,
                    name = entity.nombre,
                    price = entity.precio,
                    stock = entity.stock,
                    category = entity.categoria
                )
            }
        }

    override fun observeProductForm(): Flow<AdminProductFormContent> = formState.map { it }

    override suspend fun addProduct(item: AdminProductItem) {
        productLocalDataSource.upsert(mergeWithExisting(item))
    }

    override suspend fun updateProduct(item: AdminProductItem) {
        productLocalDataSource.upsert(mergeWithExisting(item))
    }

    override suspend fun deleteProduct(code: String) {
        productLocalDataSource.delete(code)
    }

    private suspend fun mergeWithExisting(item: AdminProductItem): ProductoEntity {
        val existing = productLocalDataSource.getById(item.code)
        val seed = ProductSeedData.productos.firstOrNull { it.id == item.code }

        fun String?.orFallback(vararg options: String): String {
            val primary = this?.takeIf { it.isNotBlank() }
            if (primary != null) return primary
            return options.firstOrNull { it.isNotBlank() } ?: ""
        }

        val attr = existing?.attr.orFallback(seed?.attr.orEmpty())
        val img = existing?.img.orFallback(seed?.img.orEmpty())
        val descripcion = existing?.descripcion.orFallback(seed?.descripcion.orEmpty())
        val stockCritico = existing?.stockCritico ?: seed?.stockCritico ?: 0

        return ProductoEntity(
            id = item.code,
            nombre = item.name,
            precio = item.price,
            categoria = item.category,
            attr = attr,
            img = img,
            stock = item.stock,
            stockCritico = stockCritico,
            descripcion = descripcion
        )
    }
}
