package com.example.milsaboresapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.example.milsaboresapp.domain.model.admin.AdminUserItem
import com.example.milsaboresapp.presentation.admin.AdminUsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsuarioScreen(
    state: AdminUsersViewModel.UiState,
    onQueryChange: (String) -> Unit,
    onRoleChange: (String?) -> Unit,
    onAddUserClick: () -> Unit,
    onDeleteUser: (String) -> Unit,
    onDismissDeleteSuccess: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuarios") },
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
            FloatingActionButton(onClick = onAddUserClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Nuevo usuario"
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
                text = "Administra los perfiles registrados",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar") },
                placeholder = { Text("RUN, nombre, correo") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )

            // Filtro por rol
            var expanded by remember { mutableStateOf(false) }
            val selectedRole = state.selectedRole ?: "Todos los roles"

            Column {
                OutlinedTextField(
                    value = selectedRole,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    label = { Text("Rol") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Ver roles"
                        )
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Todos") },
                        onClick = {
                            expanded = false
                            onRoleChange(null)
                        }
                    )
                    state.roles.forEach { role ->
                        DropdownMenuItem(
                            text = { Text(role) },
                            onClick = {
                                expanded = false
                                onRoleChange(role)
                            }
                        )
                    }
                }
            }

            if (state.isLoading) {
                Text("Cargando usuarios...")
            } else if (!state.hasUsers) {
                Text("No se encontraron usuarios.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.users, key = { it.run }) { user ->
                        AdminUserRow(item = user, onDelete = onDeleteUser)
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
                    title = { Text("Usuario eliminado") },
                    text = { Text("El registro fue eliminado correctamente.") }
                )
            }
        }
    }
}

@Composable
private fun AdminUserRow(
    item: AdminUserItem,
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
                text = "${item.run} | ${item.firstName} ${item.lastName}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Rol: ${item.role}")
                Text(text = "Región: ${item.region}")
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Comuna: ${item.commune}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.email)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { onDelete(item.run) }) {
                Text("Eliminar")
            }
        }
    }
}
