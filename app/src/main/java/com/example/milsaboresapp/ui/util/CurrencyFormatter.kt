package com.example.milsaboresapp.ui.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    private val localeCl = Locale("es", "CL")
    private val formatter = NumberFormat.getCurrencyInstance(localeCl).apply {
        maximumFractionDigits = 0
    }

    fun format(amount: Int): String = formatter.format(amount)
}
