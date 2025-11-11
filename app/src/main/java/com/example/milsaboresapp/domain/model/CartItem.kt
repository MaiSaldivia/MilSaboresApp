package com.example.milsaboresapp.domain.model

/**
 * Representa una linea del carrito con cantidad y precio unitario.
 */
data class CartItem(
    val id: String,
    val productId: String,
    val name: String,
    val variant: String,
    val message: String?,
    val unitPrice: Int,
    val quantity: Int,
    val image: String
)
