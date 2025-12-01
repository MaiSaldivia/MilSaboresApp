package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.ProductLocalDataSource
import com.example.milsaboresapp.data.local.datasource.ProductSeedData
import com.example.milsaboresapp.data.local.entity.ProductoEntity
import com.example.milsaboresapp.data.local.entity.toDomain
import com.example.milsaboresapp.data.remote.RetrofitClient
import com.example.milsaboresapp.domain.model.Producto
import com.example.milsaboresapp.domain.repository.ProductoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RemoteProductoRepositoryImpl(
    private val productLocalDataSource: ProductLocalDataSource,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) : ProductoRepository {

    init {
        scope.launch {
            syncWithRemote()
        }
    }

    override fun observeProductos(): Flow<List<Producto>> =
        productLocalDataSource.observeProducts().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun observeDestacados(limit: Int): Flow<List<Producto>> = observeProductos().map { productos ->
        productos.sortedBy { it.precio }.take(limit)
    }

    override fun observeCategorias(): Flow<List<String>> = observeProductos().map { list ->
        list.map { it.categoria }.toSet().toList().sorted()
    }

    override suspend fun findById(id: String): Producto? {
        productLocalDataSource.getById(id)?.toDomain()?.let { return it }

        val resp = RetrofitClient.apiService.getProduct(id)
        if (!resp.isSuccessful) return null

        val body = resp.body() ?: return null
        if (!isPasteleriaItem(body)) return null

        val entity = mapToEntity(body) ?: return null
        productLocalDataSource.upsert(entity)
        return entity.toDomain()
    }

    private suspend fun syncWithRemote() {
        val remoteEntities = runCatching { fetchRemotePasteleria() }.getOrDefault(emptyList())
        if (remoteEntities.isNotEmpty()) {
            productLocalDataSource.upsertAll(remoteEntities)
        }
    }

    private suspend fun fetchRemotePasteleria(): List<ProductoEntity> {
        val response = RetrofitClient.apiService.listProducts()
        if (!response.isSuccessful) return emptyList()

        val body = response.body().orEmpty()
        return body
            .filter { raw -> isPasteleriaItem(raw) }
            .mapNotNull { raw -> mapToEntity(raw) }
    }

    private fun mapToEntity(raw: Map<String, Any>): ProductoEntity? {
        val id = raw["id"]?.toString()
            ?: raw["_id"]?.toString()
            ?: return null

        val nombre = (raw["nombre"] ?: raw["name"] ?: raw["title"] ?: "")
            .toString()
            .ifBlank { "Producto sin nombre" }

        val precio = extractInt(raw["precio"] ?: raw["price"] ?: raw["valor"])

        val categoria = extractCategory(raw)?.takeIf { it.isNotBlank() } ?: "Pastelería"

        val attr = listOf(
            raw["attr"],
            raw["presentacion"],
            raw["presentación"],
            raw["tamano"],
            raw["tamaño"],
            raw["unidad"]
        )
            .mapNotNull { it?.toString() }
            .firstOrNull { it.isNotBlank() }
            .orEmpty()

        val img = resolveImage(nombre, extractImage(raw))

        val stock = extractInt(raw["stock"] ?: raw["inventario"] ?: raw["cantidad"])

        val descripcion = listOf(
            raw["descripcion"],
            raw["descripción"],
            raw["description"],
            raw["detalle"],
            raw["detalles"],
            raw["resumen"]
        )
            .mapNotNull { it?.toString() }
            .firstOrNull { it.isNotBlank() }
            .orEmpty()

        return ProductoEntity(
            id = id,
            nombre = nombre,
            precio = precio,
            categoria = categoria,
            attr = attr,
            img = img,
            stock = stock,
            stockCritico = 0,
            descripcion = descripcion
        )
    }

    private fun extractCategory(raw: Map<String, Any>): String? {
        val primaryKeys = listOf(
            "categoria",
            "category",
            "categoria_nombre",
            "categoriaNombre",
            "category_name",
            "categoryName",
            "categoria_slug",
            "category_slug",
            "tipo"
        )

        for (key in primaryKeys) {
            val resolved = resolveToString(raw[key])
            if (!resolved.isNullOrBlank()) {
                return resolved
            }
        }

        val storeCandidate = raw["tienda"] ?: raw["store"]
        if (storeCandidate is Map<*, *>) {
            val storeKeys = listOf("categoria", "category", "nombre", "name", "slug")
            for (key in storeKeys) {
                val resolved = resolveToString(storeCandidate[key])
                if (!resolved.isNullOrBlank()) {
                    return resolved
                }
            }
        } else {
            val resolved = resolveToString(storeCandidate)
            if (!resolved.isNullOrBlank()) {
                return resolved
            }
        }

        return null
    }

    private fun resolveToString(value: Any?): String? {
        return when (value) {
            is String -> value.trim().takeIf { it.isNotEmpty() }
            is Number -> value.toString()
            is Map<*, *> -> {
                val mapKeys = listOf("nombre", "name", "title", "slug", "value", "label")
                for (key in mapKeys) {
                    val resolved = resolveToString(value[key])
                    if (!resolved.isNullOrBlank()) {
                        return resolved
                    }
                }
                null
            }
            is List<*> -> {
                value.asSequence()
                    .mapNotNull { resolveToString(it) }
                    .firstOrNull { it.isNotBlank() }
            }
            else -> null
        }
    }

    private fun extractImage(raw: Map<String, Any>): String {
        val candidates = listOf(
            raw["img"],
            raw["imagen"],
            raw["image"],
            raw["images"],
            raw["imagenes"],
            raw["imagenes_principales"],
            raw["imagenesPrincipales"]
        )

        candidates.forEach { candidate ->
            val resolved = when (candidate) {
                is String -> candidate
                is List<*> -> candidate.firstOrNull()?.toString()
                is Map<*, *> -> candidate["url"]?.toString()
                else -> null
            }

            if (!resolved.isNullOrBlank()) {
                return resolved
            }
        }

        return ""
    }

    private fun extractInt(value: Any?): Int {
        return when (value) {
            is Int -> value
            is Long -> value.toInt()
            is Float -> value.toInt()
            is Double -> value.toInt()
            is Number -> value.toInt()
            is String -> value.toDoubleOrNull()?.toInt()
            else -> null
        } ?: 0
    }

    private fun resolveImage(nombre: String, extracted: String?): String {
        val cleaned = extracted?.trim().orEmpty()
        if (cleaned.isNotBlank() && !looksLikeUrl(cleaned)) {
            return cleaned
        }

        val fallback = fallbackImageForName(nombre)
        if (fallback != null) return fallback

        return ""
    }

    private fun looksLikeUrl(value: String): Boolean {
        val lower = value.lowercase()
        return lower.startsWith("http://") || lower.startsWith("https://") || lower.contains("/")
    }

    private fun fallbackImageForName(nombre: String): String? {
        val lower = nombre.lowercase()

        keywordImageMap.firstNotNullOfOrNull { (keyword, image) ->
            if (lower.contains(keyword)) image else null
        }?.let { return it }

        return ProductSeedData.productos.firstOrNull { it.nombre.equals(nombre, ignoreCase = true) }?.img
    }

    private val keywordImageMap: Map<String, String> = mapOf(
        "cheesecake" to "cheesecake",
        "mousse" to "mousse_de_chocolate",
        "torta cuadrada de chocolate" to "torta_cuadrada_de_chocolate",
        "torta vegana" to "torta_vegana_de_chocolate",
        "torta vegana de chocolate" to "torta_vegana_de_chocolate",
        "torta especial" to "torta_especial_de_cumpleanios",
        "torta sin azucar" to "torta_sin_azucar_de_naranja",
        "brownie" to "brownie"
    )

    private fun extractStoreSlug(raw: Map<String, Any>): String {
        val slugCandidates = listOf(
            raw["tienda_slug"],
            raw["tiendaSlug"],
            raw["store_slug"],
            raw["storeSlug"],
            raw["tienda"],
            raw["store"]
        )

        slugCandidates.forEach { candidate ->
            when (candidate) {
                is String -> if (candidate.isNotBlank()) return candidate
                is Map<*, *> -> {
                    val resolved = candidate["slug"]
                        ?: candidate["nombre"]
                        ?: candidate["name"]
                    if (resolved != null && resolved.toString().isNotBlank()) {
                        return resolved.toString()
                    }
                }
            }
        }

        return ""
    }

    private fun extractStringList(value: Any?): List<String> {
        return when (value) {
            is List<*> -> value.mapNotNull { it?.toString() }.filter { it.isNotBlank() }
            is Array<*> -> value.mapNotNull { it?.toString() }.filter { it.isNotBlank() }
            else -> emptyList()
        }
    }

    private fun isPasteleriaItem(raw: Map<String, Any>): Boolean {
        val keywords = listOf("torta", "pastel", "postre", "dulce", "pasteler", "queque", "cupcake", "masa", "trufa")

        val categoria = extractCategory(raw).orEmpty()
        if (keywords.any { keyword -> categoria.contains(keyword, ignoreCase = true) }) {
            return true
        }

        val tiendaSlug = extractStoreSlug(raw)
        if (keywords.any { keyword -> tiendaSlug.contains(keyword, ignoreCase = true) }) {
            return true
        }

        val nombre = (raw["nombre"] ?: raw["name"] ?: "").toString()
        if (keywords.any { keyword -> nombre.contains(keyword, ignoreCase = true) }) {
            return true
        }

        val tags = extractStringList(raw["tags"]) + extractStringList(raw["etiquetas"]) + extractStringList(raw["categorias"])
        if (tags.any { tag -> keywords.any { keyword -> tag.contains(keyword, ignoreCase = true) } }) {
            return true
        }

        return false
    }
}
