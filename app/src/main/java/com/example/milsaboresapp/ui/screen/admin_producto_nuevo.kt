package com.example.milsaboresapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.presentation.admin.AdminProductFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductoNuevoScreen(
    state: AdminProductFormViewModel.UiState,
    onCodeChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onSuccessDismiss: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.content?.title ?: "Nuevo Producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = state.content?.subtitle
                    ?: "Completa los datos del nuevo producto.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = state.code,
                onValueChange = onCodeChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Código") },
                isError = state.errors.code != null,
                supportingText = state.errors.code?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )

            OutlinedTextField(
                value = state.name,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre") },
                isError = state.errors.name != null,
                supportingText = state.errors.name?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )

            OutlinedTextField(
                value = state.price,
                onValueChange = onPriceChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = state.errors.price != null,
                supportingText = state.errors.price?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )

            OutlinedTextField(
                value = state.stock,
                onValueChange = onStockChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Stock") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = state.errors.stock != null,
                supportingText = state.errors.stock?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )

            // Selector de categoría con DropdownMenu simple
            var expanded by remember { mutableStateOf(false) }

            Box {
                OutlinedTextField(
                    value = state.category,
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
                    },
                    isError = state.errors.category != null,
                    supportingText = state.errors.category?.let {
                        { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    state.content?.categories.orEmpty().forEach { category ->
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

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSubmit,
                enabled = !state.isSubmitting,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar producto")
            }
        }

        if (state.submitSuccess) {
            AlertDialog(
                onDismissRequest = onSuccessDismiss,
                confirmButton = {
                    TextButton(onClick = onSuccessDismiss) {
                        Text("Entendido")
                    }
                },
                title = { Text("Producto registrado") },
                text = {
                    Text(
                        state.content?.successMessage
                            ?: "Registro exitoso."
                    )
                }
            )
        }
    }
}
