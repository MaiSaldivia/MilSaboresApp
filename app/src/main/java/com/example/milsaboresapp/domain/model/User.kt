// domain/model/User.kt
package com.example.milsaboresapp.domain.model

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val birthDate: String = "",
    val promoCode: String? = null,
    val acceptsPromotions: Boolean = true
    // Rol del usuario: "ADMIN" o "CLIENTE"
    ,
    val role: String = "CLIENTE",
    // Uri local de la foto de perfil
    val photoUri: String? = null
)
