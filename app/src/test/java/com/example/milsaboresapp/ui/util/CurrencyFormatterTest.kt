package com.example.milsaboresapp.ui.util

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CurrencyFormatterTest {

    @Test
    fun shouldFormatChileanPesoWithoutDecimals() {
        // When
        val formatted = CurrencyFormatter.format(12500)
        val normalized = formatted.replace('\u00A0', ' ')

        // Then
        assertTrue(normalized.contains("$"))
        assertTrue(normalized.contains("12.500"))
        assertTrue(!normalized.contains(","))
    }
}
