package com.example.milsaboresapp.domain.model.vendedor

data class VendedorDashboard(
    val title: String,
    val subtitle: String,
    val widgets: List<VendedorWidget>
)

data class VendedorWidget(
    val title: String,
    val description: String,
    val ctaLabel: String,
    val destination: VendedorDestination
)

enum class VendedorDestination {
    Home,
    Pedidos,
    Inventario
}
