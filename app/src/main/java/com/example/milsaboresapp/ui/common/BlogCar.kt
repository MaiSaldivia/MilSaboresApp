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
import com.example.milsaboresapp.R
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme

/**
 * 🎨 RÉPLICA: <article class="blog-card">
 * Componente reutilizable para mostrar un artículo de blog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogCard(
    imageResId: Int,
    category: String,
    title: String,
    onCardClick: () -> Unit
) {
    // (CSS: .blog-card)
    Card(
        modifier = Modifier
            .width(280.dp) // Un poco más ancha que la de producto
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onCardClick
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Imagen del blog
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )

            // (CSS: .blog-card__info)
            Column(modifier = Modifier.padding(16.dp)) {

                // (CSS: .blog-card__cat)
                Text(
                    text = category.uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary // (CSS: --choco)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // (CSS: .blog-card h3)
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp,
                    maxLines = 2,
                    modifier = Modifier.height(52.dp) // Altura fija
                )

                Spacer(modifier = Modifier.height(8.dp))

                // (CSS: .link)
                Text(
                    text = "Leer más",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 14.sp
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
fun PreviewBlogCard() {
    MilSaboresAppTheme {
        // Asegúrate de tener "blog1.jpg" (renombrado a blog1) en res/drawable
        BlogCard(
            imageResId = R.drawable.blog1,
            category = "Recetas",
            title = "Caso curioso #1: El misterio de la torta que desaparece",
            onCardClick = {}
        )
    }
}