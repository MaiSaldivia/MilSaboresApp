package com.example.milsaboresapp.ui.common

// Importaciones de Compose
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Importaciones del proyecto
import com.example.milsaboresapp.R // El archivo R de tus recursos
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme
import com.example.milsaboresapp.ui.theme.MilSaboresTheme

/**
 * 🎨 RÉPLICA: <div class="product-card">
 * Componente reutilizable para mostrar un producto en una lista.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    // Parámetros para hacerla reutilizable
    imageResId: Int, // El ID de la imagen en res/drawable
    title: String,
    price: String,
    onCardClick: () -> Unit // Acción al tocar la tarjeta
) {
    // (CSS: .product-card)
    Card(
        modifier = Modifier
            .width(220.dp) // Ancho fijo para la tarjeta en la fila
            .padding(MilSaboresTheme.spacing.sm),
        shape = MaterialTheme.shapes.medium, // Bordes redondeados
        elevation = CardDefaults.cardElevation(defaultElevation = MilSaboresTheme.elevations.card),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // (CSS: background: #fff)
        ),
        onClick = onCardClick // Hace que toda la tarjeta sea clickeable
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // (CSS: .product-card img)
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    // Clip para que la imagen tenga bordes redondeados arriba
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop // (CSS: object-fit: cover)
            )

            // (CSS: .product-card__info)
            Column(modifier = Modifier.padding(MilSaboresTheme.spacing.lg)) {

                // (CSS: .product-card h3)
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 16.sp, // Ajuste de tamaño
                    maxLines = 2,
                    modifier = Modifier.height(48.dp) // Altura fija para el texto
                )

                Spacer(modifier = Modifier.height(MilSaboresTheme.spacing.xs))

                // (CSS: .product-card__price)
                Text(
                    text = price,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary // (CSS: --choco)
                )
            }
        }
    }
}

// -------------------------------------------------------
// PREVISUALIZACIÓN
// -------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun PreviewProductCard() {
    MilSaboresAppTheme {
        // Asegúrate de tener una imagen en res/drawable
        // llamada "torta_vegana_de_chocolate.jpg" (¡con guion bajo!)
        ProductCard(
            imageResId = R.drawable.torta_vegana_de_chocolate,
            title = "Torta Vegana de Chocolate",
            price = "$18.000",
            onCardClick = {}
        )
    }
}