package com.example.milsaboresapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.example.milsaboresapp.presentation.admin.AdminUserFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsuarioNuevoScreen(
    state: AdminUserFormViewModel.UiState,
    onRunChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onRoleChange: (String) -> Unit,
    onRegionChange: (String) -> Unit,
    onCommuneChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onSuccessDismiss: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.content?.title ?: "Nuevo Usuario") },
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
                text = state.content?.subtitle ?: "Completa los datos de la cuenta.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = state.run,
                onValueChange = onRunChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("RUN") },
                isError = state.errors.run != null,
                supportingText = state.errors.run?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )

            OutlinedTextField(
                value = state.firstName,
                onValueChange = onFirstNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombres") },
                isError = state.errors.firstName != null,
                supportingText = state.errors.firstName?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )

            OutlinedTextField(
                value = state.lastName,
                onValueChange = onLastNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Apellidos") },
                isError = state.errors.lastName != null,
                supportingText = state.errors.lastName?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                isError = state.errors.email != null,
                supportingText = state.errors.email?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )

            // --- Rol ---
            var roleExpanded by remember { mutableStateOf(false) }

            Column {
                OutlinedTextField(
                    value = state.role,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { roleExpanded = true },
                    label = { Text("Rol") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Ver roles"
                        )
                    },
                    isError = state.errors.role != null,
                    supportingText = state.errors.role?.let {
                        { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                )

                DropdownMenu(
                    expanded = roleExpanded,
                    onDismissRequest = { roleExpanded = false }
                ) {
                    state.content?.roles.orEmpty().forEach { role ->
                        DropdownMenuItem(
                            text = { Text(role) },
                            onClick = {
                                roleExpanded = false
                                onRoleChange(role)
                            }
                        )
                    }
                }
            }

            // --- Región ---
            var regionExpanded by remember { mutableStateOf(false) }

            Column {
                OutlinedTextField(
                    value = state.region,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { regionExpanded = true },
                    label = { Text("Región") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Ver regiones"
                        )
                    },
                    isError = state.errors.region != null,
                    supportingText = state.errors.region?.let {
                        { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                )

                DropdownMenu(
                    expanded = regionExpanded,
                    onDismissRequest = { regionExpanded = false }
                ) {
                    state.content?.regions.orEmpty().forEach { region ->
                        DropdownMenuItem(
                            text = { Text(region.name) },
                            onClick = {
                                regionExpanded = false
                                onRegionChange(region.name)
                            }
                        )
                    }
                }
            }

            // --- Comuna ---
            var communeExpanded by remember { mutableStateOf(false) }

            Column {
                OutlinedTextField(
                    value = state.commune,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { communeExpanded = true },
                    label = { Text("Comuna") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Ver comunas"
                        )
                    },
                    isError = state.errors.commune != null,
                    supportingText = state.errors.commune?.let {
                        { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                )

                DropdownMenu(
                    expanded = communeExpanded,
                    onDismissRequest = { communeExpanded = false }
                ) {
                    state.availableCommunes.forEach { commune ->
                        DropdownMenuItem(
                            text = { Text(commune) },
                            onClick = {
                                communeExpanded = false
                                onCommuneChange(commune)
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
                Text("Registrar usuario")
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
                title = { Text("Usuario registrado") },
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
