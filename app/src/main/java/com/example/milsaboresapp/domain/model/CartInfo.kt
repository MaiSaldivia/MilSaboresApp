package com.example.milsaboresapp.domain.model

/**
 * Estado agregado que replica la pagina de carrito del sitio.
 */
data class CartInfo(
    val headline: String,
    val subtitle: String,
    val items: List<CartItem>,
    val summary: CartSummary,
    val shippingOptions: List<ShippingOption>,
    val paymentMethods: List<String>,
    val benefits: List<CartBenefit>,
    val assistanceMessage: String,
    val checkoutCta: String
)
