package com.example.milsaboresapp.ui.common

// Compose
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

// Theme
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme

// AppGraph para leer la sesión
import com.example.milsaboresapp.di.AppGraph

/**
 * Barra superior con marca, login / perfil y carrito.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    onLoginClick: () -> Unit,
    onCartClick: () -> Unit
) {
    // Usuario actual desde SessionManager
    val currentUser by AppGraph.sessionManager.currentUser.collectAsState()

    TopAppBar(
        title = {
            Text(
                text = "Mil Sabores",
                style = MaterialTheme.typography.displayMedium,
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        actions = {
            if (currentUser == null) {
                // No hay sesión -> botón Ingresar
                TextButton(
                    onClick = onLoginClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Ingresar",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            } else {
                // Hay sesión -> saludo + ir a perfil + cerrar sesión
                val firstName =
                    currentUser?.name?.split(" ")?.firstOrNull().orEmpty()

                Text(
                    text = "Hola, $firstName",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                TextButton(
                    onClick = onLoginClick, // navega a Perfil desde MainActivity
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Mi perfil",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                TextButton(
                    onClick = { AppGraph.sessionManager.logout() },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(
                        text = "Cerrar sesión",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            IconButton(onClick = onCartClick) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Ver Carrito",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.secondary,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

// -------------------------------------------------------
// PREVISUALIZACIÓN
// -------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun PreviewMainTopAppBar() {
    MilSaboresAppTheme {
        MainTopAppBar(
            onLoginClick = {},
            onCartClick = {}
        )
    }
}
