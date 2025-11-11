package com.example.milsaboresapp.ui.util

import androidx.annotation.DrawableRes
import com.example.milsaboresapp.R

object DrawableCatalog {

    private val images: Map<String, Int> = mapOf(
        "torta_cuadrada_de_chocolate" to R.drawable.torta_cuadrada_de_chocolate,
        "torta_cuadrada_de_frutas" to R.drawable.torta_cuadrada_de_frutas,
        "torta_circular_de_vainilla" to R.drawable.torta_circular_de_vainilla,
        "torta_circular_de_manjar" to R.drawable.torta_circular_de_manjar,
        "mousse_de_chocolate" to R.drawable.mousse_de_chocolate,
        "tiramisu_clasico" to R.drawable.tiramisu_clasico,
        "torta_sin_azucar_de_naranja" to R.drawable.torta_sin_azucar_de_naranja,
        "cheesecake" to R.drawable.cheesecake,
        "brownie" to R.drawable.brownie,
        "pan_integral" to R.drawable.pan_integral,
        "torta_vegana_de_chocolate" to R.drawable.torta_vegana_de_chocolate,
        "galletas_veganas_de_avena" to R.drawable.galletas_veganas_de_avena,
        "torta_especial_de_cumpleanios" to R.drawable.torta_especial_de_cumpleanios,
        "torta_especial_de_boda" to R.drawable.torta_especial_de_boda,
        "blog1" to R.drawable.blog1,
        "blog2" to R.drawable.blog2,
        "vitrina1" to R.drawable.vitrina1
    )

    @DrawableRes
    fun resolve(name: String?): Int {
        if (name.isNullOrBlank()) return default()
        return images[name] ?: default()
    }

    @DrawableRes
    fun default(): Int = R.drawable.logo
}
