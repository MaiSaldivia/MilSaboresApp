package com.example.milsaboresapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.domain.model.AuthBenefit
import com.example.milsaboresapp.domain.model.RegisterContent
import com.example.milsaboresapp.presentation.auth.RegisterViewModel
import com.example.milsaboresapp.ui.common.MainFooter
import com.example.milsaboresapp.ui.common.MainNavigationBar
import com.example.milsaboresapp.ui.common.MainTopAppBar
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme
import java.util.Calendar

// ---------------------------------------------------------------------
// TODAS LAS REGIONES Y COMUNAS DE CHILE
// ---------------------------------------------------------------------
private val chileRegions: Map<String, List<String>> = mapOf(
    "Arica y Parinacota" to listOf(
        "Arica", "Camarones", "Putre", "General Lagos"
    ),
    "Tarapacá" to listOf(
        "Iquique", "Alto Hospicio",
        "Pozo Almonte", "Camiña", "Colchane", "Huara", "Pica"
    ),
    "Antofagasta" to listOf(
        "Antofagasta", "Mejillones", "Sierra Gorda", "Taltal",
        "Calama", "Ollagüe", "San Pedro de Atacama",
        "Tocopilla", "María Elena"
    ),
    "Atacama" to listOf(
        "Copiapó", "Caldera", "Tierra Amarilla",
        "Chañaral", "Diego de Almagro",
        "Vallenar", "Alto del Carmen", "Freirina", "Huasco"
    ),
    "Coquimbo" to listOf(
        "La Serena", "Coquimbo", "Andacollo", "La Higuera",
        "Paihuano", "Vicuña",
        "Illapel", "Canela", "Los Vilos", "Salamanca",
        "Ovalle", "Combarbalá", "Monte Patria", "Punitaqui", "Río Hurtado"
    ),
    "Valparaíso" to listOf(
        "Valparaíso", "Casablanca", "Concón",
        "Juan Fernández", "Puchuncaví", "Quintero",
        "Viña del Mar",
        "Quilpué", "Villa Alemana", "Limache", "Olmué",
        "San Antonio", "Algarrobo", "Cartagena",
        "El Quisco", "El Tabo", "Santo Domingo",
        "Isla de Pascua",
        "Hijuelas", "La Calera", "La Cruz", "Nogales", "Quillota",
        "La Ligua", "Cabildo", "Papudo", "Petorca", "Zapallar",
        "San Felipe", "Catemu", "Llaillay", "Panquehue",
        "Putaendo", "Santa María",
        "Los Andes", "Calle Larga", "Rinconada", "San Esteban"
    ),
    "Región Metropolitana" to listOf(
        "Santiago", "Cerrillos", "Cerro Navia", "Conchalí",
        "El Bosque", "Estación Central", "Huechuraba",
        "Independencia", "La Cisterna", "La Florida", "La Granja",
        "La Pintana", "La Reina", "Las Condes", "Lo Barnechea",
        "Lo Espejo", "Lo Prado", "Macul", "Maipú", "Ñuñoa",
        "Pedro Aguirre Cerda", "Peñalolén", "Providencia",
        "Pudahuel", "Quilicura", "Quinta Normal", "Recoleta",
        "Renca", "San Joaquín", "San Miguel", "San Ramón",
        "Vitacura",
        "Puente Alto", "Pirque", "San José de Maipo",
        "Colina", "Lampa", "Tiltil",
        "San Bernardo", "Buin", "Calera de Tango", "Paine",
        "Melipilla", "Alhué", "Curacaví", "María Pinto", "San Pedro",
        "Talagante", "El Monte", "Isla de Maipo",
        "Padre Hurtado", "Peñaflor"
    ),
    "O'Higgins" to listOf(
        "Rancagua", "Codegua", "Coinco", "Coltauco",
        "Doñihue", "Graneros", "Las Cabras", "Machalí",
        "Malloa", "Mostazal", "Olivar", "Peumo", "Pichidegua",
        "Quinta de Tilcoco", "Rengo", "Requínoa", "San Vicente",
        "Pichilemu", "La Estrella", "Litueche", "Marchigüe",
        "Navidad", "Paredones",
        "San Fernando", "Chépica", "Chimbarongo", "Lolol",
        "Nancagua", "Palmilla", "Peralillo", "Placilla",
        "Pumanque", "Santa Cruz"
    ),
    "Maule" to listOf(
        "Talca", "Constitución", "Curepto", "Empedrado",
        "Maule", "Pelarco", "Pencahue", "Río Claro",
        "San Clemente", "San Rafael",
        "Cauquenes", "Chanco", "Pelluhue",
        "Curicó", "Hualañé", "Licantén", "Molina",
        "Rauco", "Romeral", "Sagrada Familia", "Teno",
        "Vichuquén",
        "Linares", "Colbún", "Longaví", "Parral",
        "Retiro", "San Javier", "Villa Alegre", "Yerbas Buenas"
    ),
    "Ñuble" to listOf(
        "Cobquecura", "Coelemu", "Ninhue", "Portezuelo",
        "Quirihue", "Ránquil", "Trehuaco",
        "Bulnes", "Chillán", "Chillán Viejo", "El Carmen",
        "Pemuco", "Quillón", "San Ignacio", "Yungay",
        "Coihueco", "Ñiquén", "San Carlos",
        "San Fabián", "San Nicolás"
    ),
    "Biobío" to listOf(
        "Concepción", "Coronel", "Chiguayante", "Florida",
        "Hualqui", "Lota", "Penco", "San Pedro de la Paz",
        "Santa Juana", "Talcahuano", "Tomé", "Hualpén",
        "Los Ángeles", "Antuco", "Cabrero", "Laja",
        "Mulchén", "Nacimiento", "Negrete", "Quilaco",
        "Quilleco", "San Rosendo", "Santa Bárbara",
        "Tucapel", "Yumbel", "Alto Biobío",
        "Arauco", "Cañete", "Contulmo", "Curanilahue",
        "Lebu", "Los Álamos", "Tirúa"
    ),
    "La Araucanía" to listOf(
        "Temuco", "Carahue", "Cholchol", "Cunco", "Curarrehue",
        "Freire", "Galvarino", "Gorbea", "Lautaro", "Loncoche",
        "Melipeuco", "Nueva Imperial", "Padre Las Casas",
        "Perquenco", "Pitrufquén", "Pucón", "Saavedra",
        "Teodoro Schmidt", "Toltén", "Vilcún", "Villarrica",
        "Angol", "Collipulli", "Curacautín", "Ercilla",
        "Lonquimay", "Los Sauces", "Lumaco", "Purén",
        "Renaico", "Traiguén", "Victoria"
    ),
    "Los Ríos" to listOf(
        "Valdivia", "Corral", "Lanco", "Los Lagos",
        "Máfil", "Mariquina", "Paillaco", "Panguipulli",
        "La Unión", "Futrono", "Lago Ranco", "Río Bueno"
    ),
    "Los Lagos" to listOf(
        "Puerto Montt", "Calbuco", "Cochamó", "Fresia",
        "Frutillar", "Llanquihue", "Los Muermos", "Maullín",
        "Puerto Varas",
        "Castro", "Ancud", "Chonchi", "Curaco de Vélez",
        "Dalcahue", "Puqueldón", "Queilén", "Quellón",
        "Quemchi", "Quinchao",
        "Osorno", "Puerto Octay", "Purranque", "Puyehue",
        "Río Negro", "San Juan de la Costa", "San Pablo",
        "Chaitén", "Futaleufú", "Hualaihué", "Palena"
    ),
    "Aysén" to listOf(
        "Coyhaique", "Lago Verde",
        "Aysén", "Cisnes", "Guaitecas",
        "Cochrane", "O'Higgins", "Tortel",
        "Chile Chico", "Río Ibáñez"
    ),
    "Magallanes" to listOf(
        "Punta Arenas", "Laguna Blanca", "Río Verde", "San Gregorio",
        "Cabo de Hornos", "Antártica",
        "Porvenir", "Primavera", "Timaukel",
        "Natales", "Torres del Paine"
    )
)

// ---------------------------------------------------------------------
// PANTALLA DE REGISTRO
// ---------------------------------------------------------------------

@Composable
fun RegistroScreen(
    state: RegisterViewModel.UiState,
    currentTab: String,
    onTabClick: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCartClick: () -> Unit,
    onRunChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onRegionChange: (String) -> Unit,
    onCommuneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPromoCodeChange: (String) -> Unit,      // 👈 NUEVO
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onAcceptPromotionsChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onNavigateToLogin: () -> Unit,
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
                RegistroLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            state.content == null -> {
                RegistroUnavailable(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            else -> {
                RegistroContentBody(
                    content = state.content,
                    state = state,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onRunChange = onRunChange,
                    onFirstNameChange = onFirstNameChange,
                    onLastNameChange = onLastNameChange,
                    onEmailChange = onEmailChange,
                    onPhoneChange = onPhoneChange,
                    onBirthDateChange = onBirthDateChange,
                    onRegionChange = onRegionChange,
                    onCommuneChange = onCommuneChange,
                    onAddressChange = onAddressChange,
                    onPromoCodeChange = onPromoCodeChange,  // 👈 NUEVO
                    onPasswordChange = onPasswordChange,
                    onConfirmPasswordChange = onConfirmPasswordChange,
                    onAcceptPromotionsChange = onAcceptPromotionsChange,
                    onSubmit = onSubmit,
                    onNavigateToLogin = onNavigateToLogin
                )
            }
        }

        if (state.submitSuccess) {
            RegistroSuccessDialog(onDismiss = onDismissSuccess)
        }
    }
}

@Composable
private fun RegistroLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Preparando formulario...", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        CircularProgressIndicator()
    }
}

@Composable
private fun RegistroUnavailable(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No pudimos cargar la informacion.",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Intenta nuevamente mas tarde.")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegistroContentBody(
    content: RegisterContent,
    state: RegisterViewModel.UiState,
    modifier: Modifier = Modifier,
    onRunChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onRegionChange: (String) -> Unit,
    onCommuneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPromoCodeChange: (String) -> Unit,      // 👈 NUEVO
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onAcceptPromotionsChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val regionList = remember { chileRegions.keys.toList() }
    val communeList = remember(state.region) {
        chileRegions[state.region] ?: emptyList()
    }

    var regionExpanded by remember { mutableStateOf(false) }
    var communeExpanded by remember { mutableStateOf(false) }

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
                        value = state.run,
                        onValueChange = onRunChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("RUN (sin puntos ni guion, Ej: 19011022K)") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.firstName,
                        onValueChange = onFirstNameChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nombres") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.lastName,
                        onValueChange = onLastNameChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Apellidos") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.email,
                        onValueChange = onEmailChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Correo electronico") },
                        singleLine = true
                    )

                    // Teléfono
                    OutlinedTextField(
                        value = state.phone,
                        onValueChange = onPhoneChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Teléfono") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )

                    BirthDateField(
                        value = state.birthDate,
                        onDateSelected = onBirthDateChange,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Región
                    ExposedDropdownMenuBox(
                        expanded = regionExpanded,
                        onExpandedChange = { regionExpanded = !regionExpanded }
                    ) {
                        OutlinedTextField(
                            value = state.region,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            label = { Text("Region") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = regionExpanded
                                )
                            },
                            singleLine = true
                        )
                        DropdownMenu(
                            expanded = regionExpanded,
                            onDismissRequest = { regionExpanded = false }
                        ) {
                            regionList.forEach { region ->
                                DropdownMenuItem(
                                    text = { Text(region) },
                                    onClick = {
                                        onRegionChange(region)
                                        regionExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Comuna
                    ExposedDropdownMenuBox(
                        expanded = communeExpanded,
                        onExpandedChange = {
                            if (communeList.isNotEmpty()) {
                                communeExpanded = !communeExpanded
                            }
                        }
                    ) {
                        OutlinedTextField(
                            value = state.commune,
                            onValueChange = {},
                            readOnly = true,
                            enabled = communeList.isNotEmpty(),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            label = { Text("Comuna") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = communeExpanded
                                )
                            },
                            singleLine = true
                        )
                        DropdownMenu(
                            expanded = communeExpanded,
                            onDismissRequest = { communeExpanded = false }
                        ) {
                            communeList.forEach { commune ->
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

                    OutlinedTextField(
                        value = state.address,
                        onValueChange = onAddressChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Direccion") },
                        singleLine = true
                    )

                    // 👇 NUEVO: Código promocional opcional
                    OutlinedTextField(
                        value = state.promoCode,
                        onValueChange = onPromoCodeChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Código promocional (opcional)") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.password,
                        onValueChange = onPasswordChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Contrasena") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    OutlinedTextField(
                        value = state.confirmPassword,
                        onValueChange = onConfirmPasswordChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Confirmar contrasena") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Text(
                        text = content.passwordHint,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = state.acceptsPromotions,
                            onCheckedChange = onAcceptPromotionsChange
                        )
                        Text("Quiero recibir novedades y descuentos")
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
                        Text(content.loginPrompt)
                        TextButton(onClick = onNavigateToLogin) {
                            Text(content.loginCta)
                        }
                    }
                }
            }
        }

        if (content.benefits.isNotEmpty()) {
            item {
                RegistroBenefitsSection(benefits = content.benefits)
            }
        }

        item {
            Text(
                text = content.legalDisclaimer,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item { MainFooter() }
    }
}

// ---------------------------------------------------------------------
// FECHA DE NACIMIENTO: MENÚS AÑO / MES / DÍA
// ---------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthDateField(
    value: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }

    val years = remember {
        (currentYear downTo currentYear - 100).map { it.toString() }
    }

    val months = remember {
        listOf(
            "01" to "Enero",
            "02" to "Febrero",
            "03" to "Marzo",
            "04" to "Abril",
            "05" to "Mayo",
            "06" to "Junio",
            "07" to "Julio",
            "08" to "Agosto",
            "09" to "Septiembre",
            "10" to "Octubre",
            "11" to "Noviembre",
            "12" to "Diciembre"
        )
    }

    val days = remember {
        (1..31).map { "%02d".format(it) }
    }

    var selectedYear by remember { mutableStateOf("") }
    var selectedMonthNum by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf("") }

    var yearExpanded by remember { mutableStateOf(false) }
    var monthExpanded by remember { mutableStateOf(false) }
    var dayExpanded by remember { mutableStateOf(false) }

    // Si llega un valor desde el ViewModel, lo parseamos
    LaunchedEffect(value) {
        if (value.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            val parts = value.split("-")
            selectedYear = parts[0]
            selectedMonthNum = parts[1]
            selectedDay = parts[2]
        }
    }

    fun updateDate() {
        if (selectedYear.isNotBlank() &&
            selectedMonthNum.isNotBlank() &&
            selectedDay.isNotBlank()
        ) {
            onDateSelected("${selectedYear}-${selectedMonthNum}-${selectedDay}")
        }
    }

    val selectedMonthLabel =
        months.firstOrNull { it.first == selectedMonthNum }?.second ?: ""

    Column(modifier = modifier) {
        Text(
            text = "Fecha de nacimiento (AAAA-MM-DD)",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // AÑO
            Box(modifier = Modifier.weight(1f)) {
                ExposedDropdownMenuBox(
                    expanded = yearExpanded,
                    onExpandedChange = { yearExpanded = !yearExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedYear,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        label = { Text("Año") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = yearExpanded
                            )
                        },
                        singleLine = true
                    )
                    DropdownMenu(
                        expanded = yearExpanded,
                        onDismissRequest = { yearExpanded = false }
                    ) {
                        years.forEach { year ->
                            DropdownMenuItem(
                                text = { Text(year) },
                                onClick = {
                                    selectedYear = year
                                    yearExpanded = false
                                    updateDate()
                                }
                            )
                        }
                    }
                }
            }

            // MES
            Box(modifier = Modifier.weight(1f)) {
                ExposedDropdownMenuBox(
                    expanded = monthExpanded,
                    onExpandedChange = { monthExpanded = !monthExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedMonthLabel,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        label = { Text("Mes") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = monthExpanded
                            )
                        },
                        singleLine = true
                    )
                    DropdownMenu(
                        expanded = monthExpanded,
                        onDismissRequest = { monthExpanded = false }
                    ) {
                        months.forEach { (num, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedMonthNum = num
                                    monthExpanded = false
                                    updateDate()
                                }
                            )
                        }
                    }
                }
            }

            // DÍA
            Box(modifier = Modifier.weight(1f)) {
                ExposedDropdownMenuBox(
                    expanded = dayExpanded,
                    onExpandedChange = { dayExpanded = !dayExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedDay,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        label = { Text("Día") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = dayExpanded
                            )
                        },
                        singleLine = true
                    )
                    DropdownMenu(
                        expanded = dayExpanded,
                        onDismissRequest = { dayExpanded = false }
                    ) {
                        days.forEach { day ->
                            DropdownMenuItem(
                                text = { Text(day) },
                                onClick = {
                                    selectedDay = day
                                    dayExpanded = false
                                    updateDate()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RegistroBenefitsSection(benefits: List<AuthBenefit>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Beneficios de registrarte",
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
private fun RegistroSuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Listo")
            }
        },
        title = { Text("Cuenta creada") },
        text = { Text("Te enviaremos un correo para confirmar tu registro.") }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewRegistroScreen() {
    val previewContent = RegisterContent(
        headline = "Crea tu cuenta Mil Sabores",
        subtitle = "Acelera cotizaciones y guarda tus pedidos favoritos.",
        submitLabel = "Registrarme",
        loginPrompt = "Ya tienes cuenta",
        loginCta = "Inicia sesion",
        passwordHint = "Tu contrasena debe tener entre 4 y 10 caracteres. Solo se permiten correos @duoc.cl, @profesor.duoc.cl o @gmail.com.",
        benefits = listOf(
            AuthBenefit("Cotizaciones rapidas", "Guarda tus datos para futuros eventos."),
            AuthBenefit("Descuentos sorpresa", "Recibe cupones en fechas especiales.")
        ),
        legalDisclaimer = "Al registrarte aceptas nuestros terminos y condiciones."
    )

    MilSaboresAppTheme {
        RegistroScreen(
            state = RegisterViewModel.UiState(
                isLoading = false,
                content = previewContent
            ),
            currentTab = "Inicio",
            onTabClick = {},
            onLoginClick = {},
            onCartClick = {},
            onRunChange = {},
            onFirstNameChange = {},
            onLastNameChange = {},
            onEmailChange = {},
            onPhoneChange = {},
            onBirthDateChange = {},
            onRegionChange = {},
            onCommuneChange = {},
            onAddressChange = {},
            onPromoCodeChange = {},            // 👈 NUEVO
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onAcceptPromotionsChange = {},
            onSubmit = {},
            onNavigateToLogin = {},
            onDismissSuccess = {}
        )
    }
}
