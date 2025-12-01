package com.example.milsaboresapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.domain.model.admin.AdminProductItem
import com.example.milsaboresapp.domain.repository.AdminProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AdminDashboardScreen(
    adminProductRepository: AdminProductRepository,
    onBack: () -> Unit
) {
    val productsState = remember { mutableStateOf(listOf<AdminProductItem>()) }

    LaunchedEffect(Unit) {
        // Observa productos
        adminProductRepository.observeProducts().collect { list ->
            productsState.value = list
        }
    }

    val code = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val price = remember { mutableStateOf(0) }
    val stock = remember { mutableStateOf(0) }
    val category = remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Admin - Gestión de productos")

        Card(colors = CardDefaults.cardColors()) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = code.value, onValueChange = { code.value = it }, label = { Text("Código") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = name.value, onValueChange = { name.value = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = price.value.toString(), onValueChange = { price.value = it.toIntOrNull() ?: 0 }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = stock.value.toString(), onValueChange = { stock.value = it.toIntOrNull() ?: 0 }, label = { Text("Stock") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = category.value, onValueChange = { category.value = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        val item = AdminProductItem(code = code.value, name = name.value, price = price.value, stock = stock.value, category = category.value)
                        CoroutineScope(Dispatchers.IO).launch {
                            adminProductRepository.addProduct(item)
                        }
                    }) {
                        Text("Agregar/Actualizar")
                    }
                    Button(onClick = { onBack() }) {
                        Text("Volver")
                    }
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(productsState.value.size) { idx ->
                val p = productsState.value[idx]
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors()) {
                    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${p.code} - ${p.name} (${p.category}) - $${p.price}")
                        Button(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                adminProductRepository.deleteProduct(p.code)
                            }
                        }) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}
