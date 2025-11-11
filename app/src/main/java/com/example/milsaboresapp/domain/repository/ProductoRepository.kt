package com.example.milsaboresapp.domain.repository

import com.example.milsaboresapp.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    fun observeProductos(): Flow<List<Producto>>
    fun observeDestacados(limit: Int = 5): Flow<List<Producto>>
    fun observeCategorias(): Flow<List<String>>
    suspend fun findById(id: String): Producto?
}
