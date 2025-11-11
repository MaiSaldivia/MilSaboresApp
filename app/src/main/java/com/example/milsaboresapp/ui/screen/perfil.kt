package com.example.milsaboresapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.domain.model.ProfileContent
import com.example.milsaboresapp.domain.model.ProfilePersonalData
import com.example.milsaboresapp.domain.model.ProfilePreferences
import com.example.milsaboresapp.domain.model.ProfileRegion
import com.example.milsaboresapp.domain.model.ProfileShippingOption
import com.example.milsaboresapp.presentation.profile.ProfileViewModel
import com.example.milsaboresapp.ui.common.MainFooter
import com.example.milsaboresapp.ui.common.MainNavigationBar
import com.example.milsaboresapp.ui.common.MainTopAppBar
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme
import com.example.milsaboresapp.ui.util.DrawableCatalog

@Composable
fun PerfilScreen(
    state: ProfileViewModel.UiState,
    currentTab: String,
    onTabClick: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCartClick: () -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onRegionChange: (String) -> Unit,
    onCommuneChange: (String) -> Unit,
    onShippingOptionChange: (String) -> Unit,
    onDefaultCouponChange: (String) -> Unit,
    onNewsletterChange: (Boolean) -> Unit,
    onSaveAddressChange: (Boolean) -> Unit,
    onSubmitProfile: () -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmitPassword: () -> Unit,
    onDismissProfileSuccess: () -> Unit,
    onDismissPasswordSuccess: () -> Unit
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
                ProfileLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            state.content == null -> {
                ProfileUnavailable(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            else -> {
                PerfilContent(
                    content = state.content,
                    uiState = state,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onFirstNameChange = onFirstNameChange,
                    onLastNameChange = onLastNameChange,
                    onPhoneChange = onPhoneChange,
                    onBirthDateChange = onBirthDateChange,
                    onAddressChange = onAddressChange,
                    onRegionChange = onRegionChange,
                    onCommuneChange = onCommuneChange,
                    onShippingOptionChange = onShippingOptionChange,
                    onDefaultCouponChange = onDefaultCouponChange,
                    onNewsletterChange = onNewsletterChange,
                    onSaveAddressChange = onSaveAddressChange,
                    onSubmitProfile = onSubmitProfile,
                    onCurrentPasswordChange = onCurrentPasswordChange,
                    onNewPasswordChange = onNewPasswordChange,
                    onConfirmPasswordChange = onConfirmPasswordChange,
                    onSubmitPassword = onSubmitPassword
                )
            }
        }

        if (state.profileSuccess) {
            AlertDialog(
                onDismissRequest = onDismissProfileSuccess,
                confirmButton = {
                    TextButton(onClick = onDismissProfileSuccess) {
                        Text("Listo")
                    }
                },
                title = { Text("Perfil actualizado") },
                text = { Text("Tus datos fueron guardados correctamente.") }
            )
        }

        if (state.passwordSuccess) {
            AlertDialog(
                onDismissRequest = onDismissPasswordSuccess,
                confirmButton = {
                    TextButton(onClick = onDismissPasswordSuccess) {
                        Text("Entendido")
                    }
                },
                title = { Text("Contraseña actualizada") },
                text = { Text("La contraseña fue modificada exitosamente.") }
            )
        }
    }
}

@Composable
private fun ProfileLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(12.dp))
        Text("Cargando tu perfil...")
    }
}

@Composable
private fun ProfileUnavailable(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No pudimos cargar tu información.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Intenta nuevamente más tarde.")
    }
}

@Composable
private fun PerfilContent(
    content: ProfileContent,
    uiState: ProfileViewModel.UiState,
    modifier: Modifier = Modifier,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onRegionChange: (String) -> Unit,
    onCommuneChange: (String) -> Unit,
    onShippingOptionChange: (String) -> Unit,
    onDefaultCouponChange: (String) -> Unit,
    onNewsletterChange: (Boolean) -> Unit,
    onSaveAddressChange: (Boolean) -> Unit,
    onSubmitProfile: () -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmitPassword: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            ProfileHero(
                headline = content.headline,
                message = content.welcomeMessage
            )
        }

        item {
            PersonalDataCard(
                content = content,
                form = uiState.personalForm,
                errors = uiState.personalErrors,
                availableCommunes = uiState.availableCommunes,
                onFirstNameChange = onFirstNameChange,
                onLastNameChange = onLastNameChange,
                onPhoneChange = onPhoneChange,
                onBirthDateChange = onBirthDateChange,
                onAddressChange = onAddressChange,
                onRegionChange = onRegionChange,
                onCommuneChange = onCommuneChange
            )
        }

        item {
            PreferencesCard(
                content = content,
                preferenceForm = uiState.preferenceForm,
                onShippingOptionChange = onShippingOptionChange,
                onDefaultCouponChange = onDefaultCouponChange,
                onNewsletterChange = onNewsletterChange,
                onSaveAddressChange = onSaveAddressChange,
                isSaving = uiState.isSavingProfile,
                onSubmit = onSubmitProfile
            )
        }

        item {
            PasswordCard(
                content = content,
                form = uiState.passwordForm,
                errors = uiState.passwordErrors,
                isUpdating = uiState.isUpdatingPassword,
                onCurrentPasswordChange = onCurrentPasswordChange,
                onNewPasswordChange = onNewPasswordChange,
                onConfirmPasswordChange = onConfirmPasswordChange,
                onSubmit = onSubmitPassword
            )
        }

        item { MainFooter() }
    }
}

@Composable
private fun ProfileHero(headline: String, message: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = DrawableCatalog.default()),
            contentDescription = "Logo Mil Sabores",
            modifier = Modifier.height(96.dp)
        )
        Text(
            text = headline,
            style = MaterialTheme.typography.displayLarge,
            fontSize = 32.sp
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/* ------------ DATOS PERSONALES (REGIÓN / COMUNA con DropdownMenu simple) ------------- */

@Composable
private fun PersonalDataCard(
    content: ProfileContent,
    form: ProfileViewModel.PersonalForm,
    errors: ProfileViewModel.PersonalErrors,
    availableCommunes: List<String>,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onRegionChange: (String) -> Unit,
    onCommuneChange: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = content.personalSectionTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = form.firstName,
                onValueChange = onFirstNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombres") },
                isError = errors.firstName != null,
                supportingText = errors.firstName?.let { { Text(it) } }
            )
            OutlinedTextField(
                value = form.lastName,
                onValueChange = onLastNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Apellidos") },
                isError = errors.lastName != null,
                supportingText = errors.lastName?.let { { Text(it) } }
            )
            OutlinedTextField(
                value = form.email,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo (no editable)") },
                enabled = false,
                readOnly = true
            )
            OutlinedTextField(
                value = form.phone,
                onValueChange = onPhoneChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            OutlinedTextField(
                value = form.birthDate,
                onValueChange = onBirthDateChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Fecha de nacimiento") }
            )
            OutlinedTextField(
                value = form.address,
                onValueChange = onAddressChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Dirección") },
                isError = errors.address != null,
                supportingText = errors.address?.let { { Text(it) } }
            )

            // Región
            var regionExpanded by remember { mutableStateOf(false) }

            Column {
                OutlinedTextField(
                    value = form.region,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { regionExpanded = true },
                    readOnly = true,
                    label = { Text("Región") },
                    isError = errors.region != null,
                    supportingText = errors.region?.let { { Text(it) } },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                )

                DropdownMenu(
                    expanded = regionExpanded,
                    onDismissRequest = { regionExpanded = false }
                ) {
                    content.regions.forEach { region ->
                        DropdownMenuItem(
                            text = { Text(region.name) },
                            onClick = {
                                onRegionChange(region.name)
                                regionExpanded = false
                            }
                        )
                    }
                }
            }

            // Comuna
            var communeExpanded by remember { mutableStateOf(false) }

            Column {
                OutlinedTextField(
                    value = form.commune,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = availableCommunes.isNotEmpty()) {
                            if (availableCommunes.isNotEmpty()) communeExpanded = true
                        },
                    readOnly = true,
                    enabled = availableCommunes.isNotEmpty(),
                    label = { Text("Comuna") },
                    isError = errors.commune != null,
                    supportingText = errors.commune?.let { { Text(it) } },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                )

                DropdownMenu(
                    expanded = communeExpanded,
                    onDismissRequest = { communeExpanded = false }
                ) {
                    availableCommunes.forEach { commune ->
                        DropdownMenuItem(
                            text = { Text(commune) },
                            onClick = {
                                onCommuneChange(commune)
                                communeExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

/* ----------------- PREFERENCIAS (ENVÍO / CUPÓN usando DropdownMenu) ----------------- */

@Composable
private fun PreferencesCard(
    content: ProfileContent,
    preferenceForm: ProfileViewModel.PreferenceForm,
    onShippingOptionChange: (String) -> Unit,
    onDefaultCouponChange: (String) -> Unit,
    onNewsletterChange: (Boolean) -> Unit,
    onSaveAddressChange: (Boolean) -> Unit,
    isSaving: Boolean,
    onSubmit: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = content.preferencesSubtitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Envío por defecto
            var shippingExpanded by remember { mutableStateOf(false) }
            val selectedShipping =
                content.shippingOptions.firstOrNull { it.id == preferenceForm.shippingOptionId }

            Column {
                OutlinedTextField(
                    value = selectedShipping?.label ?: "Selecciona una opción",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { shippingExpanded = true },
                    readOnly = true,
                    label = { Text("Envío por defecto") },
                    supportingText = { Text(content.defaultShippingHelper) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                )

                DropdownMenu(
                    expanded = shippingExpanded,
                    onDismissRequest = { shippingExpanded = false }
                ) {
                    content.shippingOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(option.label)
                                    Text(
                                        option.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            onClick = {
                                onShippingOptionChange(option.id)
                                shippingExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = preferenceForm.defaultCoupon,
                onValueChange = onDefaultCouponChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Cupón por defecto (opcional)") },
                supportingText = { Text(content.defaultCouponHelper) }
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = preferenceForm.newsletter,
                        onCheckedChange = onNewsletterChange
                    )
                    Text(text = content.newsletterLabel)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = preferenceForm.saveAddress,
                        onCheckedChange = onSaveAddressChange
                    )
                    Text(text = content.saveAddressLabel)
                }
            }

            Button(
                onClick = onSubmit,
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                } else {
                    Text(
                        text = content.saveChangesLabel,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}

/* -------------------------- TARJETA DE CONTRASEÑA -------------------------- */

@Composable
private fun PasswordCard(
    content: ProfileContent,
    form: ProfileViewModel.PasswordForm,
    errors: ProfileViewModel.PasswordErrors,
    isUpdating: Boolean,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = content.passwordSectionTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = form.current,
                onValueChange = onCurrentPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(content.currentPasswordLabel) },
                isError = errors.current != null,
                supportingText = errors.current?.let { { Text(it) } },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            OutlinedTextField(
                value = form.new,
                onValueChange = onNewPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(content.newPasswordLabel) },
                isError = errors.new != null,
                supportingText = { Text(content.passwordHelper) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            if (errors.new != null) {
                Text(
                    text = errors.new,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            OutlinedTextField(
                value = form.confirm,
                onValueChange = onConfirmPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(content.confirmPasswordLabel) },
                isError = errors.confirm != null,
                supportingText = errors.confirm?.let { { Text(it) } },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Button(
                onClick = onSubmit,
                enabled = !isUpdating,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                if (isUpdating) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                } else {
                    Text(
                        text = content.passwordSubmitLabel,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}

/* -------------------------------- PREVIEW -------------------------------- */

@Preview(showBackground = true)
@Composable
private fun PreviewPerfilScreen() {
    val sampleContent = ProfileContent(
        headline = "Mi Perfil",
        welcomeMessage = "Estás editando el perfil de camila@milsabores.cl",
        personalSectionTitle = "Datos personales",
        preferencesSubtitle = "Preferencias de compra",
        saveChangesLabel = "Guardar cambios",
        defaultShippingHelper = "Se usará en el carrito si no has elegido otro.",
        defaultCouponHelper = "Se aplicará automáticamente si está vacío el cupón.",
        newsletterLabel = "Quiero recibir promociones por email",
        saveAddressLabel = "Guardar dirección para próximos pedidos",
        passwordSectionTitle = "Cambiar contraseña",
        passwordSubmitLabel = "Actualizar contraseña",
        passwordHelper = "Tu contraseña debe tener de 4 a 10 caracteres.",
        currentPasswordLabel = "Contraseña actual",
        newPasswordLabel = "Nueva contraseña",
        confirmPasswordLabel = "Repetir nueva contraseña",
        personalData = ProfilePersonalData(
            firstName = "Camila",
            lastName = "Soto Pérez",
            email = "camila@milsabores.cl",
            phone = "+56 9 8765 4321",
            birthDate = "1994-04-18",
            address = "Pasaje Los Dulces 123, Valparaíso",
            region = "Valparaíso",
            commune = "Valparaíso"
        ),
        preferences = ProfilePreferences(
            shippingOptionId = "pickup",
            defaultCoupon = "ENVIOGRATIS",
            newsletter = true,
            saveAddress = true
        ),
        regions = listOf(
            ProfileRegion("Valparaíso", listOf("Valparaíso", "Viña del Mar")),
            ProfileRegion("Región Metropolitana", listOf("Santiago", "Providencia"))
        ),
        shippingOptions = listOf(
            ProfileShippingOption("pickup", "Retiro en tienda (gratis)", "Disponible en Valparaíso"),
            ProfileShippingOption("urban", "Envío urbano ($3.000)", "Cobertura en Gran Valparaíso")
        ),
        storedPassword = "1234"
    )

    MilSaboresAppTheme {
        PerfilScreen(
            state = ProfileViewModel.UiState(
                isLoading = false,
                content = sampleContent,
                personalForm = ProfileViewModel.PersonalForm(
                    firstName = sampleContent.personalData.firstName,
                    lastName = sampleContent.personalData.lastName,
                    email = sampleContent.personalData.email,
                    phone = sampleContent.personalData.phone,
                    birthDate = sampleContent.personalData.birthDate,
                    address = sampleContent.personalData.address,
                    region = sampleContent.personalData.region,
                    commune = sampleContent.personalData.commune
                ),
                preferenceForm = ProfileViewModel.PreferenceForm(
                    shippingOptionId = "pickup",
                    defaultCoupon = "ENVIOGRATIS",
                    newsletter = true,
                    saveAddress = true
                ),
                availableCommunes = sampleContent.regions.first().communes
            ),
            currentTab = "Inicio",
            onTabClick = {},
            onLoginClick = {},
            onCartClick = {},
            onFirstNameChange = {},
            onLastNameChange = {},
            onPhoneChange = {},
            onBirthDateChange = {},
            onAddressChange = {},
            onRegionChange = {},
            onCommuneChange = {},
            onShippingOptionChange = {},
            onDefaultCouponChange = {},
            onNewsletterChange = {},
            onSaveAddressChange = {},
            onSubmitProfile = {},
            onCurrentPasswordChange = {},
            onNewPasswordChange = {},
            onConfirmPasswordChange = {},
            onSubmitPassword = {},
            onDismissProfileSuccess = {},
            onDismissPasswordSuccess = {}
        )
    }
}
