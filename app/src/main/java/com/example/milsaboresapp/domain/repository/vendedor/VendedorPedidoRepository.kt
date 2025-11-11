package com.example.milsaboresapp.domain.repository.vendedor

import com.example.milsaboresapp.domain.model.vendedor.VendedorPedidoItem
import kotlinx.coroutines.flow.Flow

interface VendedorPedidoRepository {
    fun observePedidos(): Flow<List<VendedorPedidoItem>>
}
