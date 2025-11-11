package com.example.milsaboresapp.domain.model

data class ProfilePreferences(
    val shippingOptionId: String,
    val defaultCoupon: String,
    val newsletter: Boolean,
    val saveAddress: Boolean
)
