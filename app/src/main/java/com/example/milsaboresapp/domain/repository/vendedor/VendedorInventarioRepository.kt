package com.example.milsaboresapp.domain.repository.vendedor

import com.example.milsaboresapp.domain.model.vendedor.VendedorInventarioItem
import kotlinx.coroutines.flow.Flow

interface VendedorInventarioRepository {
    fun observeInventario(): Flow<List<VendedorInventarioItem>>
}
