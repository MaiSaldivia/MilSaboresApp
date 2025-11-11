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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.domain.model.CartBenefit
import com.example.milsaboresapp.domain.model.CartInfo
import com.example.milsaboresapp.domain.model.CartItem
import com.example.milsaboresapp.domain.model.CartSummary
import com.example.milsaboresapp.domain.model.ShippingOption
import com.example.milsaboresapp.presentation.cart.CartViewModel
import com.example.milsaboresapp.ui.common.MainFooter
import com.example.milsaboresapp.ui.common.MainNavigationBar
import com.example.milsaboresapp.ui.common.MainTopAppBar
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme
import com.example.milsaboresapp.ui.util.CurrencyFormatter
import com.example.milsaboresapp.ui.util.DrawableCatalog

@Composable
fun CarritoScreen(
    state: CartViewModel.UiState,
    currentTab: String,
    onTabClick: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCartClick: () -> Unit,
    onIncreaseQuantity: (String) -> Unit,
    onDecreaseQuantity: (String) -> Unit,
    onRemoveItem: (String) -> Unit,
    onCheckout: () -> Unit,
    onShippingOptionSelected: (ShippingOption) -> Unit,
    onContinueShopping: () -> Unit,
    onDismissSuccess: () -> Unit
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
        when {
            state.isLoading -> {
                LoadingState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            state.cartInfo == null -> {
                EmptyState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onContinueShopping = onContinueShopping
                )
            }

            else -> {
                CartContent(
                    info = state.cartInfo,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    isCheckoutProcessing = state.isCheckoutProcessing,
                    onIncreaseQuantity = onIncreaseQuantity,
                    onDecreaseQuantity = onDecreaseQuantity,
                    onRemoveItem = onRemoveItem,
                    onCheckout = onCheckout,
                    onShippingOptionSelected = onShippingOptionSelected,
                    onContinueShopping = onContinueShopping
                )
            }
        }

        if (state.checkoutSuccess) {
            CheckoutSuccessDialog(onDismiss = onDismissSuccess)
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(12.dp))
        Text("Preparando tu carrito...")
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier,
    onContinueShopping: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tu carrito esta vacio",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Agrega tus tortas y postres favoritos para seguir.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onContinueShopping) {
            Text("Ver catalogo")
        }
    }
}

@Composable
private fun CartContent(
    info: CartInfo,
    modifier: Modifier = Modifier,
    isCheckoutProcessing: Boolean,
    onIncreaseQuantity: (String) -> Unit,
    onDecreaseQuantity: (String) -> Unit,
    onRemoveItem: (String) -> Unit,
    onCheckout: () -> Unit,
    onShippingOptionSelected: (ShippingOption) -> Unit,
    onContinueShopping: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = info.headline,
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 32.sp
                )
                Text(
                    text = info.subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (info.items.isNotEmpty()) {
            items(info.items, key = { it.id }) { item ->
                CartItemCard(
                    item = item,
                    onIncrease = { onIncreaseQuantity(item.id) },
                    onDecrease = { onDecreaseQuantity(item.id) },
                    onRemove = { onRemoveItem(item.id) }
                )
            }
        } else {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Aun no tienes productos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Explora el catalogo y vuelve cuando estes listo para endulzar tu evento.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        OutlinedButton(onClick = onContinueShopping) {
                            Text("Ir a Productos")
                        }
                    }
                }
            }
        }

        item {
            SummarySection(
                summary = info.summary,
                isCheckoutProcessing = isCheckoutProcessing,
                checkoutLabel = info.checkoutCta,
                hasItems = info.items.isNotEmpty(),
                onCheckout = onCheckout,
                onContinueShopping = onContinueShopping
            )
        }

        if (info.shippingOptions.isNotEmpty()) {
            item {
                ShippingSection(
                    options = info.shippingOptions,
                    onOptionSelected = onShippingOptionSelected
                )
            }
        }

        if (info.paymentMethods.isNotEmpty()) {
            item { PaymentSection(methods = info.paymentMethods) }
        }

        if (info.benefits.isNotEmpty()) {
            item { BenefitsSection(benefits = info.benefits) }
        }

        item {
            AssistanceSection(
                message = info.assistanceMessage,
                onContinueShopping = onContinueShopping
            )
        }

        item { MainFooter() }
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = DrawableCatalog.resolve(item.image)),
                    contentDescription = item.name,
                    modifier = Modifier.size(88.dp)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (item.variant.isNotBlank()) {
                        Text(
                            text = item.variant,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    item.message?.takeIf { it.isNotBlank() }?.let { message ->
                        Text(
                            text = "Mensaje: $message",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Quitar producto"
                    )
                }
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuantitySelector(
                    quantity = item.quantity,
                    onDecrease = onDecrease,
                    onIncrease = onIncrease
                )
                Text(
                    text = CurrencyFormatter.format(item.unitPrice * item.quantity),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun QuantitySelector(
    quantity: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(onClick = onDecrease) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Disminuir"
            )
        }
        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        IconButton(onClick = onIncrease) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Aumentar"
            )
        }
    }
}

@Composable
private fun SummarySection(
    summary: CartSummary,
    isCheckoutProcessing: Boolean,
    checkoutLabel: String,
    hasItems: Boolean,
    onCheckout: () -> Unit,
    onContinueShopping: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Resumen del pedido",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            SummaryRow(label = "Subtotal", value = CurrencyFormatter.format(summary.subtotal))
            SummaryRow(
                label = "Descuento",
                value = "- ${CurrencyFormatter.format(summary.discount)}"
            )
            SummaryRow(
                label = "Despacho",
                value = CurrencyFormatter.format(summary.deliveryFee)
            )
            HorizontalDivider()
            SummaryRow(
                label = "Total",
                value = CurrencyFormatter.format(summary.total),
                emphasize = true
            )

            summary.notes.forEach { note ->
                Text(
                    text = "- $note",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = onCheckout,
                enabled = hasItems && !isCheckoutProcessing,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                if (isCheckoutProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                } else {
                    Text(checkoutLabel, color = MaterialTheme.colorScheme.onSecondary)
                }
            }

            OutlinedButton(
                onClick = onContinueShopping,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seguir comprando")
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, emphasize: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (emphasize) MaterialTheme.typography.titleMedium
            else MaterialTheme.typography.bodyMedium,
            fontWeight = if (emphasize) FontWeight.SemiBold else FontWeight.Normal
        )
        Text(
            text = value,
            style = if (emphasize) MaterialTheme.typography.titleMedium
            else MaterialTheme.typography.bodyMedium,
            fontWeight = if (emphasize) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun ShippingSection(
    options: List<ShippingOption>,
    onOptionSelected: (ShippingOption) -> Unit
) {
    var selectedOption by remember { mutableStateOf(options.firstOrNull()) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Opciones de entrega",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        options.forEach { option ->
            val selected = option == selectedOption

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selected,
                        onClick = {
                            selectedOption = option
                            onOptionSelected(option)
                        },
                        role = Role.RadioButton
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (selected)
                        MaterialTheme.colorScheme.surfaceVariant
                    else
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selected,
                        onClick = null
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = option.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = option.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Costo: ${CurrencyFormatter.format(option.fee)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }

        selectedOption?.let {
            Text(
                text = "Seleccionado: ${it.title}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun PaymentSection(methods: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Metodos de pago",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                methods.forEach { method ->
                    Text(
                        text = "- $method",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun BenefitsSection(benefits: List<CartBenefit>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Por que elegirnos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            benefits.forEach { benefit ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = benefit.icon,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = benefit.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = benefit.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AssistanceSection(
    message: String,
    onContinueShopping: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Necesitas ayuda",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = onContinueShopping) {
                Text("Seguir viendo opciones")
            }
        }
    }
}

@Composable
private fun CheckoutSuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Volver")
            }
        },
        title = { Text("Pedido registrado") },
        text = { Text("Te contactaremos por correo para coordinar el pago y entrega.") }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewCarritoScreen() {
    val sampleInfo = CartInfo(
        headline = "Tu carrito listo para celebrar",
        subtitle = "Revisa cantidades, mensajes y metodos de entrega antes de continuar.",
        items = listOf(
            CartItem(
                id = "1",
                productId = "TE001",
                name = "Torta Especial de Cumpleanos",
                variant = "30 porciones - Mensaje: Feliz cumple Nico",
                message = "Feliz cumple Nico",
                unitPrice = 55000,
                quantity = 1,
                image = "torta_especial_de_cumpleanios"
            ),
        ),
        summary = CartSummary(
            subtotal = 55000,
            discount = 0,
            deliveryFee = 4500,
            total = 59500,
            notes = listOf(
                "Despachamos de martes a domingo en Gran Valparaiso."
            )
        ),
        shippingOptions = listOf(
            ShippingOption(
                title = "Despacho a domicilio",
                description = "Cobertura en Gran Valparaiso.",
                fee = 4500
            ),
            ShippingOption(
                title = "Retiro en taller",
                description = "Sin costo en Av. Independencia 456.",
                fee = 0
            )
        ),
        paymentMethods = listOf("Transferencia bancaria", "Webpay", "Efectivo al retirar"),
        benefits = listOf(
            CartBenefit(
                icon = "🎂",
                title = "Pasteleria artesanal",
                description = "Elaboramos cada pedido a mano el mismo dia de la entrega."
            )
        ),
        assistanceMessage = "Si necesitas un formato especial escribenos a contacto@milsabores.cl o por WhatsApp.",
        checkoutCta = "Continuar con el pago"
    )

    MilSaboresAppTheme {
        CarritoScreen(
            state = CartViewModel.UiState(isLoading = false, cartInfo = sampleInfo),
            currentTab = "Inicio",
            onTabClick = {},
            onLoginClick = {},
            onCartClick = {},
            onIncreaseQuantity = {},
            onDecreaseQuantity = {},
            onRemoveItem = {},
            onCheckout = {},
            onShippingOptionSelected = {},
            onContinueShopping = {},
            onDismissSuccess = {}
        )
    }
}
