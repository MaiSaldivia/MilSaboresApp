package com.example.milsaboresapp.data.local.datasource

import com.example.milsaboresapp.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Fuente de datos en memoria que simula LocalStorage/browser storage.
 * Expone flujos reactivos para que la UI se mantenga sincronizada.
 */
class InMemoryProductDataSource(
    seed: List<ProductoEntity> = ProductSeedData.productos
) {

    private val productsState = MutableStateFlow(
        seed.associateBy { it.id }
    )

    fun observeAll(): Flow<List<ProductoEntity>> =
        productsState.map { map ->
            map.values
                .sortedWith(compareBy({ it.categoria }, { it.nombre }))
        }

    suspend fun findById(id: String): ProductoEntity? = productsState.value[id]

    suspend fun upsert(entity: ProductoEntity) {
        productsState.update { current ->
            current + (entity.id to entity)
        }
    }

    suspend fun delete(id: String) {
        productsState.update { current ->
            if (current.containsKey(id)) current - id else current
        }
    }
}
