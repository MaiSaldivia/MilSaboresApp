package com.example.milsaboresapp.navigation

sealed class AppDestination(val topTab: String) {
    object Home : AppDestination("Inicio")
    object Productos : AppDestination("Productos")
    data class ProductoDetalle(val productId: String) : AppDestination("Productos")
    object Nosotros : AppDestination("Nosotros")
    object Blog : AppDestination("Blog")
    data class BlogDetalle(val postId: String) : AppDestination("Blog")
    object Contacto : AppDestination("Contacto")
    object Login : AppDestination("Inicio")
    object Registro : AppDestination("Inicio")
    object Carrito : AppDestination("Inicio")
    object Perfil : AppDestination("Inicio")
    object Admin : AppDestination("Inicio")
}
