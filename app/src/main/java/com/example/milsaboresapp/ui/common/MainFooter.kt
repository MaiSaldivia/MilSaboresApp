package com.example.milsaboresapp.ui.common

// Importaciones de Compose
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Importación del Theme (CSS)
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme
import com.example.milsaboresapp.ui.theme.MilSaboresTheme

/**
 * 🎨 RÉPLICA: <footer class="footer">
 * Componente reutilizable para el pie de página de la tienda.
 */
@Composable
fun MainFooter() {

    // Variable para guardar el estado del input del newsletter
    var email by remember { mutableStateOf("") }

    // Columna principal que envuelve todo el footer
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MilSaboresTheme.extendedColors.footerBackground)
            .padding(horizontal = MilSaboresTheme.spacing.xl, vertical = MilSaboresTheme.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Columna 1: Enlaces y Marca
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // (CSS: .brand__name--footer)
            Text(
                text = "Mil Sabores",
                style = MaterialTheme.typography.displayLarge, // Usa la fuente Pacifico
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.secondary // (CSS: --choco)
            )

            // (CSS: .footer__links)
            Column(
                modifier = Modifier.padding(vertical = MilSaboresTheme.spacing.md),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val linkColor = MaterialTheme.colorScheme.primary
                Text("Tortas Cuadradas", color = linkColor)
                Text("Tortas Circulares", color = linkColor)
                Text("Sin Azúcar", color = linkColor)
                Text("Veganas", color = linkColor)
                Text("Sin Gluten", color = linkColor)
            }

            // (CSS: .pay)
            Text("💳 💳 🏦", fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(MilSaboresTheme.spacing.xxl))

        // Columna 2: Newsletter (CSS: .newsletter)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // (CSS: .newsletter__label)
            Text(
                text = "Recibe actualizaciones, novedades y promociones.",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = MilSaboresTheme.spacing.md)
            )

            // (CSS: .newsletter__row)
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Ingresa tu email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.small,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MilSaboresTheme.extendedColors.border,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(MilSaboresTheme.spacing.sm))

                // (CSS: .btn--primary)
                Button(
                    onClick = { /* TODO: Lógica de suscripción */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Suscribirse", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(MilSaboresTheme.spacing.xxl))

        // (CSS: .copy)
        Text(
            text = "© 2025 Pastelería Mil Sabores",
            style = MaterialTheme.typography.bodySmall,
            color = MilSaboresTheme.extendedColors.mutedText
        )
    }
}

// -------------------------------------------------------
// PREVISUALIZACIÓN
// -------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun PreviewMainFooter() {
    MilSaboresAppTheme {
        MainFooter()
    }
}