// Ubicación: com/example/milsaboresapp/ui/theme/Color.kt
package com.example.milsaboresapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * 🎨 Paleta base inspirada en los CSS originales de la web
 */
object MilSaboresColorTokens {
    // Variables globales (`:root`)
    val BackgroundCream = Color(0xFFFFF5E1)   // --bg
    val AccentPetal = Color(0xFFFFC0CB)       // --accent
    val BrandCocoa = Color(0xFF8B4513)        // --choco / --brand
    val BrandCocoa600 = Color(0xFF6F3F2E)     // --brand-600
    val TextBody = Color(0xFF5D4037)          // --text
    val Muted = Color(0xFFB0BEC5)             // --muted
    val Surface = Color(0xFFFFFFFF)           // --white / --surface
    val Border = Color(0xFFE6DDCB)            // --line

    // Superficies específicas de secciones
    val HeroPanel = Color(0xFFEEE5D7)
    val CardInset = Color(0xFFEEE7DA)
    val Footer = Color(0xFFF7EFE1)

    // Estados y utilidades
    val SuccessContainer = Color(0xFFE6F7E9)
    val SuccessContent = Color(0xFF216E3A)
    val DangerContainer = Color(0xFFFDECEA)
    val DangerContent = Color(0xFF611A15)
}

/**
 * Colores extendidos (no cubiertos por el `ColorScheme` de Material).
 */
data class MilSaboresExtendedColors(
    val border: Color = MilSaboresColorTokens.Border,
    val heroBackground: Color = MilSaboresColorTokens.HeroPanel,
    val cardInsetBackground: Color = MilSaboresColorTokens.CardInset,
    val footerBackground: Color = MilSaboresColorTokens.Footer,
    val mutedText: Color = MilSaboresColorTokens.Muted,
    val successContainer: Color = MilSaboresColorTokens.SuccessContainer,
    val successContent: Color = MilSaboresColorTokens.SuccessContent,
    val dangerContainer: Color = MilSaboresColorTokens.DangerContainer,
    val dangerContent: Color = MilSaboresColorTokens.DangerContent
)

val LocalExtendedColors = staticCompositionLocalOf { MilSaboresExtendedColors() }

/**
 * Esquema de colores claro (único soporte por ahora).
 */
val LightColors: ColorScheme = lightColorScheme(
    primary = MilSaboresColorTokens.BrandCocoa,
    onPrimary = Color.White,
    primaryContainer = MilSaboresColorTokens.BrandCocoa.copy(alpha = 0.90f),
    onPrimaryContainer = Color.White,

    secondary = MilSaboresColorTokens.AccentPetal,
    onSecondary = MilSaboresColorTokens.BrandCocoa,
    secondaryContainer = MilSaboresColorTokens.AccentPetal.copy(alpha = 0.75f),
    onSecondaryContainer = MilSaboresColorTokens.BrandCocoa,

    tertiary = MilSaboresColorTokens.Footer,
    onTertiary = MilSaboresColorTokens.TextBody,
    tertiaryContainer = MilSaboresColorTokens.HeroPanel,
    onTertiaryContainer = MilSaboresColorTokens.TextBody,

    background = MilSaboresColorTokens.BackgroundCream,
    onBackground = MilSaboresColorTokens.TextBody,

    surface = MilSaboresColorTokens.Surface,
    onSurface = MilSaboresColorTokens.TextBody,
    surfaceVariant = MilSaboresColorTokens.CardInset,
    onSurfaceVariant = MilSaboresColorTokens.TextBody,

    outline = MilSaboresColorTokens.Border,
    outlineVariant = MilSaboresColorTokens.Border,

    error = MilSaboresColorTokens.DangerContent,
    onError = Color.White,
    errorContainer = MilSaboresColorTokens.DangerContainer,
    onErrorContainer = MilSaboresColorTokens.DangerContent,

    inverseSurface = MilSaboresColorTokens.BrandCocoa,
    inverseOnSurface = Color.White,
    inversePrimary = MilSaboresColorTokens.AccentPetal
)