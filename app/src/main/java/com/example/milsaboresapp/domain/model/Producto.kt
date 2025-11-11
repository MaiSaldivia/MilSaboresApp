package com.example.milsaboresapp.domain.model

/**
 * Modelo de Dominio (limpio) que usa la app.
 * No sabe nada de bases de datos.
 */
data class Producto(
    val id: String,
    val nombre: String,
    val precio: Int,
    val categoria: String,
    val attr: String,
    val img: String, // Nombre del drawable, ej: "torta_vegana_de_chocolate"
    val stock: Int,
    val stockCritico: Int,
    val descripcion: String
)