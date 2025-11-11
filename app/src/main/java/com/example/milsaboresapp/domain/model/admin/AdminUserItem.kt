package com.example.milsaboresapp.domain.model.admin

data class AdminUserItem(
    val run: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String,
    val region: String = "",
    val commune: String = ""
)
