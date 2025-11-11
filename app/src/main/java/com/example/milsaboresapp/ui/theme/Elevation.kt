package com.example.milsaboresapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppElevations(
    val level0: Dp = 0.dp,
    val level1: Dp = 1.dp,
    val card: Dp = 4.dp,
    val raised: Dp = 8.dp,
    val floating: Dp = 16.dp
)

val LocalElevations = staticCompositionLocalOf { AppElevations() }
