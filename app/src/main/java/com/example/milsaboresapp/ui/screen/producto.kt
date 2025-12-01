package com.example.milsaboresapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.presentation.productdetail.ProductDetailViewModel.UiState
import com.example.milsaboresapp.ui.common.MainFooter
import com.example.milsaboresapp.ui.common.MainNavigationBar
import com.example.milsaboresapp.ui.common.MainTopAppBar
import com.example.milsaboresapp.ui.common.ProductCard
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme
import com.example.milsaboresapp.ui.util.CurrencyFormatter
import com.example.milsaboresapp.ui.util.DrawableCatalog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    state: UiState,
    currentTab: String,
    onTabClick: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCartClick: () -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToCategory: (String) -> Unit,
    onQuantityChange: (Int) -> Unit,
    onMessageChange: (String) -> Unit,
    onAddToCart: () -> Unit,
    onRelatedClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
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
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                state.product?.let { producto ->
                    item {
                        ProductDetailContent(
                            title = producto.nombre,
                            category = producto.categoria,
                            attribute = producto.attr,
                            price = CurrencyFormatter.format(producto.precio),
                            description = producto.descripcion,
                            imageResId = DrawableCatalog.resolve(producto.img),
                            quantity = state.quantity,
                            customMessage = state.customMessage,
                            showCustomMessage = state.showCustomMessageField,
                            onBackClick = onNavigateToProducts,
                            onCategoryClick = { onNavigateToCategory(producto.categoria) },
                            onQuantityChange = onQuantityChange,
                            onMessageChange = onMessageChange,
                            onAddToCart = onAddToCart
                        )
                    }

                    if (state.related.isNotEmpty()) {
                        item {
                            Text(
                                text = "También te puede gustar",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(state.related) { related ->
                                    ProductCard(
                                        imageResId = DrawableCatalog.resolve(related.img),
                                        title = related.nombre,
                                        price = CurrencyFormatter.format(related.precio)
                                    ) {
                                        onRelatedClick(related.id)
                                    }
                                }
                            }
                        }
                    }
                } ?: item {
                    Text(
                        text = "No encontramos el producto.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                item {
                    MainFooter()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun ProductDetailContent(
    title: String,
    category: String,
    attribute: String,
    price: String,
    description: String,
    imageResId: Int,
    quantity: Int,
    customMessage: String,
    showCustomMessage: Boolean,
    onBackClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onQuantityChange: (Int) -> Unit,
    onMessageChange: (String) -> Unit,
    onAddToCart: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
        ) {
            Text("Volver")
        }
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Image(
            painter = painterResource(id = imageResId),
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentScale = ContentScale.Crop
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(onClick = onCategoryClick) {
                Text(category, maxLines = 1)
            }
            if (attribute.isNotBlank()) {
                Text(
                    text = "• $attribute",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = price,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge
        )

        if (showCustomMessage) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Mensaje personalizado (opcional)",
                    style = MaterialTheme.typography.labelLarge
                )
                OutlinedTextField(
                    value = customMessage,
                    onValueChange = onMessageChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ej: ¡Feliz cumpleaños Ana! 🎉") },
                    maxLines = 1,
                    singleLine = true
                )
            }
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = quantity.toString(),
                onValueChange = { text -> text.toIntOrNull()?.let(onQuantityChange) },
                modifier = Modifier
                    .widthIn(min = 96.dp, max = 140.dp),
                label = { Text("Cantidad") },
                singleLine = true,
                maxLines = 1
            )
            Button(
                onClick = onAddToCart,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Añadir al carrito",
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1
                )
            }
        }

        ShareRow()
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun ShareRow() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Compartir",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("📲 Compartir", "📸 Instagram", "📋 Copiar enlace").forEach { label ->
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(label, maxLines = 1)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewProductDetailScreen() {
    MilSaboresAppTheme {
        ProductDetailScreen(
            state = UiState(
                isLoading = false,
                product = com.example.milsaboresapp.domain.model.Producto(
                    id = "TC001",
                    nombre = "Torta Cuadrada de Chocolate",
                    precio = 45000,
                    categoria = "Tortas Cuadradas",
                    attr = "20 porciones",
                    img = "torta_cuadrada_de_chocolate",
                    stock = 6,
                    stockCritico = 2,
                    descripcion = "Deliciosa torta de chocolate con capas de ganache."
                ),
                related = emptyList(),
                quantity = 1,
                customMessage = "",
                showCustomMessageField = true
            ),
            currentTab = "Productos",
            onTabClick = {},
            onLoginClick = {},
            onCartClick = {},
            onNavigateToProducts = {},
            onNavigateToCategory = {},
            onQuantityChange = {},
            onMessageChange = {},
            onAddToCart = {},
            onRelatedClick = {}
        )
    }
}
