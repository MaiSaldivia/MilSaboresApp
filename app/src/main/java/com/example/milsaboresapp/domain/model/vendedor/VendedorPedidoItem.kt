package com.example.milsaboresapp.domain.model.vendedor

data class VendedorPedidoItem(
    val id: String,
    val cliente: String,
    val total: Int,
    val estado: String,
    val detalle: List<VendedorPedidoDetalle>
)

data class VendedorPedidoDetalle(
    val producto: String,
    val cantidad: Int,
    val precio: Int
)
