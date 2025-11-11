package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.VendedorSeedData
import com.example.milsaboresapp.domain.model.vendedor.VendedorInventarioItem
import com.example.milsaboresapp.domain.repository.vendedor.VendedorInventarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class VendedorInventarioRepositoryImpl : VendedorInventarioRepository {

    private val inventarioState = MutableStateFlow(VendedorSeedData.inventario)

    override fun observeInventario(): Flow<List<VendedorInventarioItem>> = inventarioState.map { it }
}
