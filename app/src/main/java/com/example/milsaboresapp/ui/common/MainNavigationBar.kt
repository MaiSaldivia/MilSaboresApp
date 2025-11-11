package com.example.milsaboresapp.ui.common

// Importaciones de Compose
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Importación del Theme (CSS)
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme

/**
 * 🎨 RÉPLICA: <nav class="nav">
 * Es la barra de pestañas con los enlaces de navegación.
 */
@Composable
fun MainNavigationBar(
    // La pantalla actual, para saber qué pestaña marcar
    currentScreen: String = "Inicio",
    // Función que se llamará al tocar una pestaña
    onTabClick: (String) -> Unit
) {
    // Lista de las pantallas de la navegación principal
    val screens = listOf("Inicio", "Productos", "Nosotros", "Blog", "Contacto")

    // Variable para guardar el índice de la pestaña seleccionada
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentScreen) {
        selectedTabIndex = screens.indexOf(currentScreen).coerceAtLeast(0)
    }

    // El CSS original usa "overflow-x: auto" en móvil.
    // Esto se replica en Compose con un 'ScrollableTabRow'.
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        // Color de fondo
        containerColor = MaterialTheme.colorScheme.surface,
        // Color del indicador de la pestaña activa
        contentColor = MaterialTheme.colorScheme.primary,
        // Quita el padding de los bordes
        edgePadding = 0.dp
    ) {
        screens.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    onTabClick(title) // Avisa que se cambió de pestaña
                },
                text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 14.sp
                    )
                }
            )
        }
    }
}

// -------------------------------------------------------
// PREVISUALIZACIÓN
// -------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun PreviewMainNavigationBar() {
    MilSaboresAppTheme {
        MainNavigationBar(
            currentScreen = "Inicio",
            onTabClick = {}
        )
    }
}