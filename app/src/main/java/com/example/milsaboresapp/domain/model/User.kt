// domain/model/User.kt
package com.example.milsaboresapp.domain.model

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val birthDate: String = "",
    val promoCode: String? = null,
    val acceptsPromotions: Boolean = true
)
