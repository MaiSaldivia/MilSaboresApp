package com.example.milsaboresapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.milsaboresapp.domain.model.vendedor.VendedorPedidoItem
import com.example.milsaboresapp.presentation.vendedor.VendedorPedidosViewModel
import com.example.milsaboresapp.ui.util.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendedorPedidosScreen(
    state: VendedorPedidosViewModel.UiState,
    onSelectPedido: (String) -> Unit,
    onDismissDetalle: () -> Unit,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedidos Pendientes") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Inicio")
                    }
                },
                actions = {
                    TextButton(onClick = onHome) {
                        Text("Tienda")
                    }
                    TextButton(onClick = onLogout) {
                        Text("Cerrar sesion")
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Cargando pedidos...")
                    }
                }

                state.pedidos.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No hay pedidos pendientes.")
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.pedidos) { pedido ->
                            PedidoRow(pedido = pedido, onSelect = onSelectPedido)
                        }
                    }
                }
            }

            val selected = state.selectedPedido
            if (selected != null) {
                PedidoDetalleDialog(pedido = selected, onDismiss = onDismissDetalle)
            }
        }
    }
}

@Composable
private fun PedidoRow(
    pedido: VendedorPedidoItem,
    onSelect: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${pedido.id} | ${pedido.cliente}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Estado: ${pedido.estado}")
                Text(text = CurrencyFormatter.format(pedido.total))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { onSelect(pedido.id) }) {
                Text("Ver detalle")
            }
        }
    }
}

@Composable
private fun PedidoDetalleDialog(
    pedido: VendedorPedidoItem,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Entendido")
            }
        },
        title = { Text("Detalle del pedido ${pedido.id}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Cliente: ${pedido.cliente}")
                Text("Estado: ${pedido.estado}")
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    pedido.detalle.forEach { item ->
                        Text("- ${item.cantidad} x ${item.producto} (${CurrencyFormatter.format(item.precio)})")
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Total: ${CurrencyFormatter.format(pedido.total)}",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}
