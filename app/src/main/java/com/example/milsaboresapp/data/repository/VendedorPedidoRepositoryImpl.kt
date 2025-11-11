package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.VendedorSeedData
import com.example.milsaboresapp.domain.model.vendedor.VendedorPedidoItem
import com.example.milsaboresapp.domain.repository.vendedor.VendedorPedidoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class VendedorPedidoRepositoryImpl : VendedorPedidoRepository {

    private val pedidosState = MutableStateFlow(VendedorSeedData.pedidos)

    override fun observePedidos(): Flow<List<VendedorPedidoItem>> = pedidosState.map { it }
}
