package com.example.milsaboresapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.milsaboresapp.domain.model.Producto

/**
 * Entity de Room para la tabla de productos,
 * basado en los campos de data.js
 */
@Entity(tableName = "productos")
    data class ProductoEntity(
// Usamos el 'id' de tu data.js (ej: "TC001") como PrimaryKey
@PrimaryKey
    val id: String,
    val nombre: String,
    val precio: Int,
    val categoria: String,
    val attr: String,
    val img: String,
    val stock: Int,
    val stockCritico: Int,
    val descripcion: String
)

/**
 * Convierte ProductoEntity (Base de Datos) a modelo de dominio Producto (App)
 */
fun ProductoEntity.toDomain(): Producto {
    return Producto(
        id = id,
        nombre = nombre,
        precio = precio,
        categoria = categoria,
        attr = attr,
        img = img,
        stock = stock,
        stockCritico = stockCritico,
        descripcion = descripcion
    )
}

/**
 * Convierte modelo de dominio Producto (App) a ProductoEntity (Base de Datos)
 */
fun Producto.toEntity(): ProductoEntity {
    return ProductoEntity(
        id = id,
        nombre = nombre,
        precio = precio,
        categoria = categoria,
        attr = attr,
        img = img,
        stock = stock,
        stockCritico = stockCritico,
        descripcion = descripcion
    )
}