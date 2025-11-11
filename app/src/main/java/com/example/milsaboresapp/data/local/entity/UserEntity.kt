package com.example.milsaboresapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val run: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String,
    val phone: String = "",
    val birthDate: String = "",
    val region: String = "",
    val commune: String = "",
    val address: String = "",
    val promoCode: String? = null,        // 👈 NUEVO
    val acceptsPromotions: Boolean = true,// 👈 NUEVO
    val password: String
)
