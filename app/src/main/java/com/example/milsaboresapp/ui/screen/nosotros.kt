package com.example.milsaboresapp.ui.screen

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.domain.model.AboutInfo
import com.example.milsaboresapp.domain.model.Achievement
import com.example.milsaboresapp.domain.model.TeamMember
import com.example.milsaboresapp.domain.model.TimelineEvent
import com.example.milsaboresapp.domain.model.ValueItem
import com.example.milsaboresapp.presentation.about.AboutViewModel
import com.example.milsaboresapp.ui.common.MainFooter
import com.example.milsaboresapp.ui.common.MainNavigationBar
import com.example.milsaboresapp.ui.common.MainTopAppBar
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme
import com.example.milsaboresapp.ui.util.DrawableCatalog

@Composable
fun NosotrosScreen(
    state: AboutViewModel.UiState,
    currentTab: String,
    onTabClick: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCartClick: () -> Unit,
    onContactClick: () -> Unit
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
                NosotrosLoading(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding))
            }

            state.info == null -> {
                NosotrosEmpty(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding))
            }

            else -> {
                NosotrosContent(
                    info = state.info,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onContactClick = onContactClick
                )
            }
        }
    }
}

@Composable
private fun NosotrosLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Cargando historia...")
    }
}

@Composable
private fun NosotrosEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No encontramos informacion disponible.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Intenta nuevamente mas tarde.")
    }
}

@Composable
private fun NosotrosContent(
    info: AboutInfo,
    modifier: Modifier = Modifier,
    onContactClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        item {
            HeroAboutSection(
                title = info.headline,
                subtitle = info.subtitle
            )
        }

        item {
            MissionVisionSection(
                mission = info.mission,
                vision = info.vision
            )
        }

        item {
            TimelineSection(events = info.timeline)
        }

        if (info.values.isNotEmpty()) {
            item {
                ValuesSection(values = info.values)
            }
        }

        if (info.achievements.isNotEmpty()) {
            item {
                AchievementsSection(achievements = info.achievements)
            }
        }

        if (info.team.isNotEmpty()) {
            item {
                TeamSection(team = info.team)
            }
        }

        item {
            CallToActionSection(
                message = info.callToActionMessage,
                buttonLabel = info.callToActionButton,
                onContactClick = onContactClick
            )
        }

        item {
            MainFooter()
        }
    }
}

@Composable
private fun HeroAboutSection(
    title: String,
    subtitle: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Image(
            painter = painterResource(id = DrawableCatalog.resolve("vitrina1")),
            contentDescription = "Tienda Mil Sabores",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = title,
            style = MaterialTheme.typography.displayMedium,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MissionVisionSection(
    mission: String,
    vision: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Nuestra esencia",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
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
                    text = "Mision",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = mission,
                    style = MaterialTheme.typography.bodyMedium
                )
                Divider()
                Text(
                    text = "Vision",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = vision,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun TimelineSection(events: List<TimelineEvent>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Nuestro recorrido",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            events.forEach { event ->
                TimelineCard(event = event)
            }
        }
    }
}

@Composable
private fun TimelineCard(event: TimelineEvent) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = event.year,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ValuesSection(values: List<ValueItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Lo que nos mueve",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            values.forEach { value ->
                ValueCard(item = value)
            }
        }
    }
}

@Composable
private fun ValueCard(item: ValueItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun AchievementsSection(achievements: List<Achievement>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Logros que celebramos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            achievements.forEach { achievement ->
                AchievementCard(item = achievement)
            }
        }
    }
}

@Composable
private fun AchievementCard(item: Achievement) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TeamSection(team: List<TeamMember>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Nuestro equipo",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            team.forEach { member ->
                TeamMemberCard(member = member)
            }
        }
    }
}

@Composable
private fun TeamMemberCard(member: TeamMember) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = member.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = member.role,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = member.bio,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun CallToActionSection(
    message: String,
    buttonLabel: String,
    onContactClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Button(
                onClick = onContactClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(
                    text = buttonLabel,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewNosotrosScreen() {
    val sampleInfo = AboutInfo(
        headline = "Celebramos con sabor desde 2010",
        subtitle = "Pasteleria familiar de Valparaiso con foco en experiencias personalizadas.",
        mission = "Crear reposteria artesanal que acompane momentos unicos.",
        vision = "Ser la pasteleria referente en la region por calidad y cercania.",
        timeline = listOf(
            TimelineEvent(year = "2010", title = "Nace el taller", description = "Primera cocina en la casa de la abuela."),
            TimelineEvent(year = "2014", title = "Primer local", description = "Abrimos nuestra tienda en el centro."),
            TimelineEvent(year = "2023", title = "Mil Sabores App", description = "Lanzamos nuestra app para compras online.")
        ),
        values = listOf(
            ValueItem(title = "Ingredientes honestos", description = "Trabajamos con productores locales."),
            ValueItem(title = "Disenos a medida", description = "Cada pedido refleja tu estilo."),
            ValueItem(title = "Sostenibilidad", description = "Reducimos desperdicios en cada proceso.")
        ),
        team = listOf(
            TeamMember(name = "Carolina Saldivia", role = "Maestra pastelera", bio = "Crea las recetas insignia desde 2010."),
            TeamMember(name = "Luis Yanez", role = "Director creativo", bio = "Lidera la experiencia personalizada."),
            TeamMember(name = "Antonia Rivas", role = "Coordinadora de eventos", bio = "Coordina entregas y celebraciones.")
        ),
        achievements = listOf(
            Achievement(title = "+1200 celebraciones felices", description = "Acompanamos eventos en toda la region."),
            Achievement(title = "Receta premiada", description = "Nuestra torta vegana recibio menciones locales."),
            Achievement(title = "Academia Mil Sabores", description = "Mas de 300 alumnos pasaron por nuestros talleres.")
        ),
        callToActionMessage = "Quieres planificar tu proxima celebracion con nosotros? Nuestro equipo esta listo para ayudarte.",
        callToActionButton = "Escribenos"
    )

    MilSaboresAppTheme {
        NosotrosScreen(
            state = AboutViewModel.UiState(isLoading = false, info = sampleInfo),
            currentTab = "Nosotros",
            onTabClick = {},
            onLoginClick = {},
            onCartClick = {},
            onContactClick = {}
        )
    }
}
