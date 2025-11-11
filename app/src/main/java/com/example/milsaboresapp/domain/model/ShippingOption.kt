// domain/model/ShippingOption.kt
package com.example.milsaboresapp.domain.model

data class ShippingOption(
    val title: String,
    val description: String,
    val fee: Int = 0
)
