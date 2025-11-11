package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.InMemoryProductDataSource
import com.example.milsaboresapp.data.local.datasource.ProductSeedData
import com.example.milsaboresapp.data.local.entity.toDomain
import com.example.milsaboresapp.domain.model.Producto
import com.example.milsaboresapp.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductoRepositoryImpl(
    private val localDataSource: InMemoryProductDataSource
) : ProductoRepository {

    private val categoriasSeed = ProductSeedData.categorias

    override fun observeProductos(): Flow<List<Producto>> =
        localDataSource.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeDestacados(limit: Int): Flow<List<Producto>> =
        observeProductos().map { productos ->
            productos
                .sortedBy { it.precio }
                .take(limit)
        }

    override fun observeCategorias(): Flow<List<String>> =
        observeProductos().map { productos ->
            val fromProductos = productos.map { it.categoria }.toSet()
            (categoriasSeed + fromProductos)
                .distinct()
                .sorted()
        }

    override suspend fun findById(id: String): Producto? =
        localDataSource.findById(id)?.toDomain()
}
