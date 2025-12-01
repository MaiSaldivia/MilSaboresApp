package com.example.milsaboresapp.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.presentation.products.ProductsViewModel
import com.example.milsaboresapp.presentation.products.ProductsViewModel.SortOption
import com.example.milsaboresapp.ui.common.MainFooter
import com.example.milsaboresapp.ui.common.MainNavigationBar
import com.example.milsaboresapp.ui.common.MainTopAppBar
import com.example.milsaboresapp.ui.common.ProductCard
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme
import com.example.milsaboresapp.ui.util.CurrencyFormatter
import com.example.milsaboresapp.ui.util.DrawableCatalog

@Composable
fun ProductsScreen(
    state: ProductsViewModel.UiState,
    currentTab: String,
    onTabClick: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCartClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onSortSelected: (SortOption) -> Unit,
    onProductClick: (String) -> Unit,
    onAddToCartClick: (String) -> Unit
) {
    val categoryOptions: List<String> = remember(state.categories) {
        deriveCategoryOptions(state.categories)
    }

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = "Productos",
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 32.sp
                )
            }

            item {
                ProductsSearchBar(
                    query = state.query,
                    onQueryChange = onQueryChange,
                    categories = categoryOptions,
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = onCategorySelected,
                    sort = state.sort,
                    onSortSelected = onSortSelected
                )
            }

            if (categoryOptions.isNotEmpty()) {
                item {
                    CategorySuggestions(
                        categories = categoryOptions,
                        selected = state.selectedCategory,
                        onChipClick = onCategorySelected
                    )
                }
            }

            when {
                state.isLoading -> {
                    item { BoxLoading() }
                }

                state.products.isEmpty() -> {
                    item { EmptyResults() }
                }

                else -> {
                    itemsIndexed(state.products.chunked(2)) { _, row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            row.forEach { producto ->
                                ProductCatalogCard(
                                    title = producto.nombre,
                                    description = producto.attr,
                                    price = CurrencyFormatter.format(producto.precio),
                                    imageResId = DrawableCatalog.resolve(producto.img),
                                    modifier = Modifier.weight(1f),
                                    onClick = { onProductClick(producto.id) },
                                    onAddToCart = { onAddToCartClick(producto.id) }
                                )
                            }
                            if (row.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            item { MainFooter() }
        }
    }
}

@Composable
private fun ProductsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    sort: SortOption,
    onSortSelected: (SortOption) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar producto") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CategoryDropdown(
                modifier = Modifier.weight(1f),
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected
            )

            SortDropdown(
                modifier = Modifier.weight(1f),
                current = sort,
                onSelected = onSortSelected
            )
        }
    }
}

@Composable
private fun CategoryDropdown(
    modifier: Modifier = Modifier,
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val label = selectedCategory ?: "Todas las categorías"

    Box(modifier = modifier) {
        OutlinedTextField(
            value = label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoría") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Ver categorías"
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Todas las categorías") },
                onClick = {
                    onCategorySelected(null)
                    expanded = false
                }
            )
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SortDropdown(
    modifier: Modifier = Modifier,
    current: SortOption,
    onSelected: (SortOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val label = when (current) {
        SortOption.NameAsc -> "Orden: A - Z"
        SortOption.NameDesc -> "Orden: Z - A"
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Ordenar") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Ver opciones de orden"
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SortOption.values().forEach { option ->
                val optionLabel = when (option) {
                    SortOption.NameAsc -> "Orden A - Z"
                    SortOption.NameDesc -> "Orden Z - A"
                }
                DropdownMenuItem(
                    text = { Text(optionLabel) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun deriveCategoryOptions(rawCategories: List<String>): List<String> {
    if (rawCategories.isEmpty()) return emptyList()

    val ordered = listOf(
        "Postres Individuales",
        "Productos Sin Azúcar",
        "Productos Sin Gluten",
        "Productos Vegano",
        "Pastelería Tradicional",
        "Tortas"
    )

    val hasTortas = rawCategories.any { it.contains("Torta", ignoreCase = true) }
    val normalizedSet = rawCategories.map { category ->
        when {
            category.equals("Productos Vegana", ignoreCase = true) -> "Productos Vegano"
            category.contains("Torta", ignoreCase = true) -> "Tortas"
            else -> category
        }
    }.toMutableSet()

    if (!hasTortas) {
        normalizedSet.remove("Tortas")
    }

    return ordered.filter { normalizedSet.contains(it) }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategorySuggestions(
    categories: List<String>,
    selected: String?,
    onChipClick: (String?) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SuggestionChip(
            text = "Todas",
            selected = selected == null,
            onClick = { onChipClick(null) }
        )

        categories.forEach { category ->
            SuggestionChip(
                text = category,
                selected = selected?.equals(category, ignoreCase = true) == true,
                onClick = { onChipClick(category) }
            )
        }
    }
}

@Composable
private fun SuggestionChip(text: String, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    Surface(
        color = background,
        contentColor = contentColor,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, borderColor),
        onClick = onClick
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun ProductCatalogCard(
    title: String,
    description: String,
    price: String,
    imageResId: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Column(modifier = modifier) {
        ProductCard(
            imageResId = imageResId,
            title = title,
            price = price,
            onCardClick = onClick
        )
        if (description.isNotBlank()) {
            Text(
                text = description,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Button(
            onClick = onAddToCart,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Añadir al carrito", color = MaterialTheme.colorScheme.onSecondary)
        }
    }
}

@Composable
private fun BoxLoading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Cargando productos…")
    }
}

@Composable
private fun EmptyResults() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No encontramos productos con esos filtros.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Prueba con otra búsqueda o restablece los filtros.")
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewProductsScreen() {
    MilSaboresAppTheme {
        ProductsScreen(
            state = ProductsViewModel.UiState(
                isLoading = false,
                query = "",
                selectedCategory = null,
                sort = SortOption.NameAsc,
                categories = listOf(
                    "Tortas Cuadradas",
                    "Tortas Circulares",
                    "Postres Individuales",
                    "Productos Sin Azúcar",
                    "Pastelería Tradicional",
                    "Productos Sin Gluten",
                    "Productos Vegana",
                    "Tortas Especiales"
                ),
                products = emptyList()
            ),
            currentTab = "Productos",
            onTabClick = {},
            onLoginClick = {},
            onCartClick = {},
            onQueryChange = {},
            onCategorySelected = {},
            onSortSelected = {},
            onProductClick = {},
            onAddToCartClick = {}
        )
    }
}
