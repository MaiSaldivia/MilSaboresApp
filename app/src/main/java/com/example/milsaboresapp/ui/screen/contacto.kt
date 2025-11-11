package com.example.milsaboresapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.domain.model.ContactChannel
import com.example.milsaboresapp.domain.model.ContactInfo
import com.example.milsaboresapp.presentation.contact.ContactViewModel
import com.example.milsaboresapp.ui.common.MainFooter
import com.example.milsaboresapp.ui.common.MainNavigationBar
import com.example.milsaboresapp.ui.common.MainTopAppBar
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactoScreen(
    state: ContactViewModel.UiState,
    currentTab: String,
    onTabClick: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCartClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEventTypeChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSubmit: () -> Unit,
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
                ContactLoading(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding))
            }

            state.contactInfo == null -> {
                ContactEmpty(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding))
            }

            else -> {
                ContactContent(
                    info = state.contactInfo,
                    uiState = state,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onNameChange = onNameChange,
                    onEmailChange = onEmailChange,
                    onPhoneChange = onPhoneChange,
                    onEventTypeChange = onEventTypeChange,
                    onMessageChange = onMessageChange,
                    onSubmit = onSubmit
                )
            }
        }

        if (state.submitSuccess) {
            ContactSuccessDialog(onDismiss = onDismissSuccess)
        }
    }
}

@Composable
private fun ContactLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Preparando formulario...")
    }
}

@Composable
private fun ContactEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No pudimos cargar la informacion de contacto.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Intenta actualizar mas tarde.")
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ContactContent(
    info: ContactInfo,
    uiState: ContactViewModel.UiState,
    modifier: Modifier = Modifier,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEventTypeChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = info.headline,
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 32.sp
                )
                Text(
                    text = info.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Datos de contacto",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    ContactInfoRow(label = "Direccion", value = info.address)
                    ContactInfoRow(label = "Telefono", value = info.phone)
                    ContactInfoRow(label = "Correo", value = info.email)
                    ContactInfoRow(label = "Horario", value = info.businessHours)
                    Text(
                        text = "Cobertura",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        info.serviceAreas.forEach { area ->
                            ServiceChip(area)
                        }
                    }
                }
            }
        }

        if (info.channels.isNotEmpty()) {
            item {
                ChannelsSection(channels = info.channels)
            }
        }

        item {
            ContactForm(
                name = uiState.name,
                email = uiState.email,
                phone = uiState.phone,
                eventType = uiState.eventType,
                message = uiState.message,
                isSubmitting = uiState.isSubmitting,
                onNameChange = onNameChange,
                onEmailChange = onEmailChange,
                onPhoneChange = onPhoneChange,
                onEventTypeChange = onEventTypeChange,
                onMessageChange = onMessageChange,
                onSubmit = onSubmit
            )
        }

        item { MainFooter() }
    }
}

@Composable
private fun ContactInfoRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ServiceChip(text: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ChannelsSection(channels: List<ContactChannel>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Tambien estamos en",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            channels.forEach { channel ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = channel.label,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = channel.value,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactForm(
    name: String,
    email: String,
    phone: String,
    eventType: String,
    message: String,
    isSubmitting: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEventTypeChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Cuentanos sobre tu evento",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Correo electronico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("Telefono (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = eventType,
            onValueChange = onEventTypeChange,
            label = { Text("Tipo de evento (matrimonio, cumpleanos, etc.)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            label = { Text("Mensaje") },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            maxLines = 6
        )
        Button(
            onClick = onSubmit,
            enabled = !isSubmitting && name.isNotBlank() && email.isNotBlank() && message.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.height(18.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            } else {
                Text("Enviar mensaje", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    }
}

@Composable
private fun ContactSuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Listo")
            }
        },
        title = { Text("Mensaje enviado") },
        text = { Text("Gracias por escribirnos. Te contactaremos dentro de las proximas 24 horas.") }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewContactoScreen() {
    val sampleInfo = ContactInfo(
        headline = "Hablemos de tu proxima celebracion",
        description = "Nuestro equipo responde en menos de 24 horas.",
        address = "Av. Independencia 456, Valparaiso",
        phone = "+56 9 8765 4321",
        email = "contacto@milsabores.cl",
        businessHours = "Lunes a Sabado, 09:00 a 19:00 hrs",
        serviceAreas = listOf("Gran Valparaiso", "Vina del Mar"),
        channels = listOf(
            ContactChannel(label = "WhatsApp", value = "+56 9 8765 4321"),
            ContactChannel(label = "Instagram", value = "@milsabores.cl")
        )
    )
    MilSaboresAppTheme {
        ContactoScreen(
            state = ContactViewModel.UiState(isLoading = false, contactInfo = sampleInfo),
            currentTab = "Contacto",
            onTabClick = {},
            onLoginClick = {},
            onCartClick = {},
            onNameChange = {},
            onEmailChange = {},
            onPhoneChange = {},
            onEventTypeChange = {},
            onMessageChange = {},
            onSubmit = {},
            onDismissSuccess = {}
        )
    }
}
