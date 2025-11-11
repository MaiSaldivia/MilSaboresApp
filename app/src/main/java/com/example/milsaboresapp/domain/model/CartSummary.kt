package com.example.milsaboresapp.domain.model

/**
 * Totales del carrito calculados a partir de los items.
 */
data class CartSummary(
    val subtotal: Int,
    val discount: Int,
    val deliveryFee: Int,
    val total: Int,
    val notes: List<String>
)
