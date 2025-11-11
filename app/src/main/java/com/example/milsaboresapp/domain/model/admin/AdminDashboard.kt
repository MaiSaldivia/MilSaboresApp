package com.example.milsaboresapp.domain.model.admin

data class AdminDashboard(
    val title: String,
    val subtitle: String,
    val widgets: List<AdminWidget>
)

data class AdminWidget(
    val title: String,
    val description: String,
    val ctaLabel: String,
    val destination: AdminDestination
)

enum class AdminDestination {
    Home,
    Productos,
    Usuarios,
    Login
}
