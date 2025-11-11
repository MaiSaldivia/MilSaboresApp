package com.example.milsaboresapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onNavigateHome: () -> Unit,
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
                item {
                    Breadcrumb(
                        productName = state.product?.nombre ?: "Producto",
                        category = state.product?.categoria,
                        onHomeClick = onNavigateHome,
                        onProductsClick = onNavigateToProducts,
                        onCategoryClick = { cat -> onNavigateToCategory(cat) }
                    )
                }

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
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                content = {
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
                            )
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

@Composable
private fun Breadcrumb(
    productName: String,
    category: String?,
    onHomeClick: () -> Unit,
    onProductsClick: () -> Unit,
    onCategoryClick: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextButton(onClick = onHomeClick) {
            Text("Inicio")
        }
        Text("›")
        TextButton(onClick = onProductsClick) {
            Text("Productos")
        }
        if (!category.isNullOrBlank()) {
            Text("›")
            TextButton(onClick = { onCategoryClick(category) }) {
                Text(category)
            }
        }
        Text("›")
        Text(productName, fontWeight = FontWeight.SemiBold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
    onCategoryClick: () -> Unit,
    onQuantityChange: (Int) -> Unit,
    onMessageChange: (String) -> Unit,
    onAddToCart: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TextButton(onClick = onCategoryClick) {
                Text(category)
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = quantity.toString(),
                onValueChange = { text -> text.toIntOrNull()?.let(onQuantityChange) },
                modifier = Modifier
                    .width(96.dp),
                label = { Text("Cantidad") },
                singleLine = true,
                maxLines = 1
            )
            Button(
                onClick = onAddToCart,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Añadir al carrito", color = MaterialTheme.colorScheme.onPrimary)
            }
        }

        ShareRow()
    }
}

@Composable
private fun ShareRow() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Compartir",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("📲 Compartir", "📸 Instagram", "📋 Copiar enlace").forEach { label ->
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(label)
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
            onNavigateHome = {},
            onNavigateToProducts = {},
            onNavigateToCategory = {},
            onQuantityChange = {},
            onMessageChange = {},
            onAddToCart = {},
            onRelatedClick = {}
        )
    }
}
