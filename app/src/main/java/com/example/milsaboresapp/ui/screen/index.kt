package com.example.milsaboresapp.ui.screen

// Importaciones de Compose
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// =======================================================
//   ¡AQUÍ ESTÁ LA CORRECCIÓN!
//   Estas líneas ahora apuntan a la carpeta "ui/common"
// =======================================================
import com.example.milsaboresapp.ui.common.MainFooter
import com.example.milsaboresapp.ui.common.MainNavigationBar
import com.example.milsaboresapp.ui.common.MainTopAppBar
import com.example.milsaboresapp.ui.common.ProductCard
import com.example.milsaboresapp.ui.common.BlogCard

import com.example.milsaboresapp.presentation.home.HomeViewModel
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme
import com.example.milsaboresapp.ui.util.CurrencyFormatter
import com.example.milsaboresapp.ui.util.DrawableCatalog

/**
 * 🎨 RÉPLICA: index.html
 * (Ensambla todas las piezas)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndexScreen(
    state: HomeViewModel.HomeUiState,
    currentTab: String,
    onTabClick: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCartClick: () -> Unit,
    onHeroCtaClick: () -> Unit,
    onProductClick: (String) -> Unit,
    onBlogClick: (String) -> Unit
) {

    // Esqueleto de la pantalla
    Scaffold(
        topBar = {
            // PIEZA 1: El Header (desde "common")
            Column {
                MainTopAppBar(
                    onLoginClick = onLoginClick,
                    onCartClick = onCartClick
                )
                MainNavigationBar(
                    currentScreen = currentTab,
                    onTabClick = onTabClick
                )
            }
        },
        // Usamos el color de fondo del Theme
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        // Contenido principal con scroll
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            // PIEZA 2: El Hero Section
            HeroSection(
                imageResId = DrawableCatalog.resolve(state.hero.imageResName),
                title = state.hero.title,
                ctaLabel = state.hero.ctaLabel,
                onHeroCtaClick = onHeroCtaClick
            )

            // PIEZA 3: La Rejilla de Productos
            HomeScreenSectionTitle(title = "Nuestros Destacados")
            if (state.destacados.isEmpty() && state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(state.destacados, key = { it.id }) { producto ->
                        ProductCard(
                            imageResId = DrawableCatalog.resolve(producto.img),
                            title = producto.nombre,
                            price = CurrencyFormatter.format(producto.precio)
                        ) {
                            onProductClick(producto.id)
                        }
                    }
                }
            }

            // PIEZA 4: La Rejilla del Blog
            HomeScreenSectionTitle(title = "Nuestro Blog")
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(state.blogDestacados, key = { it.id }) { post ->
                    BlogCard(
                        imageResId = DrawableCatalog.resolve(post.imageResName),
                        category = post.category,
                        title = post.title
                    ) {
                        onBlogClick(post.id)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // PIEZA 5: El Pie de Página (desde "common")
            MainFooter() // <-- Esta línea ahora funciona
        }
    }
}

// =======================================================
//   Piezas que solo se usan en esta pantalla
// =======================================================

@Composable
fun HeroSection(
    imageResId: Int,
    title: String,
    ctaLabel: String,
    onHeroCtaClick: () -> Unit
) {
    // (CSS: .hero-alt)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        // (Asegúrate de tener "vitrina1.jpg" renombrada a "vitrina1" en res/drawable)
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Vitrina Mil Sabores",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // (CSS: .title-alt)
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge,
                fontSize = 32.sp, // Ajuste
                color = Color.White,
                textAlign = TextAlign.Center
            )
            // (CSS: .btn--secondary)
            Button(
                onClick = onHeroCtaClick,
                modifier = Modifier.padding(top = 16.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.secondary // Color --choco
                )
            ) {
                Text(ctaLabel)
            }
        }
    }
}

@Composable
fun HomeScreenSectionTitle(title: String) {
    // (CSS: .section-title)
    Text(
        text = title,
        style = MaterialTheme.typography.displayLarge, // Usa la fuente Pacifico
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    )
}

// -------------------------------------------------------
// PREVISUALIZACIÓN
// -------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun PreviewIndexScreen() {
    MilSaboresAppTheme {
        IndexScreen(
            state = HomeViewModel.HomeUiState(),
            currentTab = "Inicio",
            onTabClick = {},
            onLoginClick = {},
            onCartClick = {},
            onHeroCtaClick = {},
            onProductClick = {},
            onBlogClick = {}
        )
    }
}