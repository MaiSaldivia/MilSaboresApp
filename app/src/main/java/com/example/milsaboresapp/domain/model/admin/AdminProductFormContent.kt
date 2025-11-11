package com.example.milsaboresapp.domain.model.admin

data class AdminProductFormContent(
    val title: String,
    val subtitle: String,
    val categories: List<String>,
    val successMessage: String
)
