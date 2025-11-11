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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import com.example.milsaboresapp.domain.model.AuthBenefit
import com.example.milsaboresapp.domain.model.LoginContent
import com.example.milsaboresapp.presentation.auth.LoginViewModel
import com.example.milsaboresapp.ui.common.MainFooter
import com.example.milsaboresapp.ui.common.MainNavigationBar
import com.example.milsaboresapp.ui.common.MainTopAppBar
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme

@Composable
fun LoginScreen(
    state: LoginViewModel.UiState,
    currentTab: String,
    onTabClick: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCartClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onNavigateToRegister: () -> Unit,
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
                LoginLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            state.content == null -> {
                LoginUnavailable(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            else -> {
                LoginContentBody(
                    content = state.content,
                    state = state,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onRememberChange = onRememberChange,
                    onSubmit = onSubmit,
                    onNavigateToRegister = onNavigateToRegister
                )
            }
        }

        if (state.submitSuccess) {
            LoginSuccessDialog(onDismiss = onDismissSuccess)
        }
    }
}

@Composable
private fun LoginLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cargando contenido...", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        CircularProgressIndicator()
    }
}

@Composable
private fun LoginUnavailable(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No pudimos cargar la información.",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Intenta de nuevo más tarde.")
    }
}

@Composable
private fun LoginContentBody(
    content: LoginContent,
    state: LoginViewModel.UiState,
    modifier: Modifier = Modifier,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = content.headline,
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 32.sp
                )
                Text(
                    text = content.subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = onEmailChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Correo electrónico") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = onPasswordChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Contraseña") },
                        singleLine = true
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = state.remember,
                            onCheckedChange = onRememberChange
                        )
                        Text(content.rememberLabel)
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = { }) {
                            Text(content.forgotPasswordLabel)
                        }
                    }
                    state.errorMessage?.let { error ->
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Button(
                        onClick = onSubmit,
                        enabled = !state.isSubmitting,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        if (state.isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.height(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        } else {
                            Text(
                                content.submitLabel,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                    Divider()
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(content.registerPrompt)
                        TextButton(onClick = onNavigateToRegister) {
                            Text(content.registerCta)
                        }
                    }
                }
            }
        }

        if (content.benefits.isNotEmpty()) {
            item {
                AuthBenefitsSection(benefits = content.benefits)
            }
        }

        item {
            Text(
                text = content.supportMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item { MainFooter() }
    }
}

@Composable
private fun AuthBenefitsSection(benefits: List<AuthBenefit>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Ventajas de iniciar sesión",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            benefits.forEach { benefit ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = benefit.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = benefit.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginSuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Continuar")
            }
        },
        title = { Text("Bienvenido") },
        text = {
            Text("Sesión iniciada correctamente. Pronto podrás gestionar tus pedidos.")
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoginScreen() {
    val previewContent = LoginContent(
        headline = "Bienvenido de vuelta",
        subtitle = "Revisa tus pedidos y sigue planificando celebraciones dulces.",
        rememberLabel = "Recordarme",
        forgotPasswordLabel = "Olvidé mi contraseña",
        submitLabel = "Ingresar",
        registerPrompt = "Aún no tienes cuenta",
        registerCta = "Crear una cuenta",
        supportMessage = "Escribe a soporte@milsabores.cl si necesitas ayuda.",
        benefits = listOf(
            AuthBenefit("Historial de pedidos", "Guarda tus tortas favoritas."),
            AuthBenefit("Seguimiento", "Recibe notificaciones de estado.")
        )
    )

    MilSaboresAppTheme {
        LoginScreen(
            state = LoginViewModel.UiState(
                isLoading = false,
                content = previewContent
            ),
            currentTab = "Inicio",
            onTabClick = {},
            onLoginClick = {},
            onCartClick = {},
            onEmailChange = {},
            onPasswordChange = {},
            onRememberChange = {},
            onSubmit = {},
            onNavigateToRegister = {},
            onDismissSuccess = {}
        )
    }
}
