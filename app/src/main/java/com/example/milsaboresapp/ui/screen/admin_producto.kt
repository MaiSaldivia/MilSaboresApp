package com.example.milsaboresapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.domain.model.admin.AdminProductItem
import com.example.milsaboresapp.presentation.admin.AdminProductsViewModel
import com.example.milsaboresapp.ui.util.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductosScreen(
    state: AdminProductsViewModel.UiState,
    onQueryChange: (String) -> Unit,
    onCategoryChange: (String?) -> Unit,
    onAddProductClick: () -> Unit,
    onDeleteProduct: (String) -> Unit,
    onDismissDeleteSuccess: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProductClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Nuevo producto"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Gestiona el inventario de productos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar") },
                placeholder = { Text("Código, nombre o categoría") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )

            // Filtro por categoría con DropdownMenu simple
            var expanded by remember { mutableStateOf(false) }
            val currentCategory = state.selectedCategory ?: "Todas las categorías"

            Box {
                OutlinedTextField(
                    value = currentCategory,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    label = { Text("Categoría") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Ver categorías"
                        )
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Todas") },
                        onClick = {
                            expanded = false
                            onCategoryChange(null)
                        }
                    )
                    state.categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                expanded = false
                                onCategoryChange(category)
                            }
                        )
                    }
                }
            }

            if (state.isLoading) {
                Text("Cargando productos...")
            } else if (!state.hasProducts) {
                Text("No se encontraron productos.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.products, key = { it.code }) { item ->
                        AdminProductRow(item = item, onDelete = onDeleteProduct)
                    }
                }
            }
        }

        if (state.deleteSuccess) {
            AlertDialog(
                onDismissRequest = onDismissDeleteSuccess,
                confirmButton = {
                    TextButton(onClick = onDismissDeleteSuccess) {
                        Text("Entendido")
                    }
                },
                title = { Text("Producto eliminado") },
                text = { Text("El registro fue eliminado correctamente.") }
            )
        }
    }
}

@Composable
private fun AdminProductRow(
    item: AdminProductItem,
    onDelete: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${item.code} | ${item.name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Categoría: ${item.category}")
                Text(text = "Stock: ${item.stock}")
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Precio: ${CurrencyFormatter.format(item.price)}")
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { onDelete(item.code) }) {
                Text("Eliminar")
            }
        }
    }
}
