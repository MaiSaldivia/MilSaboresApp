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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.data.local.datasource.BlogDetailSeedData
import com.example.milsaboresapp.domain.model.BlogDetail
import com.example.milsaboresapp.domain.model.BlogDetailHighlight
import com.example.milsaboresapp.domain.model.BlogDetailRelated
import com.example.milsaboresapp.domain.model.BlogDetailSection
import com.example.milsaboresapp.presentation.blogdetail.BlogDetailViewModel
import com.example.milsaboresapp.ui.common.MainFooter
import com.example.milsaboresapp.ui.common.MainNavigationBar
import com.example.milsaboresapp.ui.common.MainTopAppBar
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme
import com.example.milsaboresapp.ui.util.DrawableCatalog

@Composable
fun BlogDetalle1Screen(
    state: BlogDetailViewModel.UiState,
    currentTab: String,
    onTabClick: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCartClick: () -> Unit,
    onBackToBlog: () -> Unit,
    onRelatedClick: (String) -> Unit,
    onContactClick: () -> Unit
) {
    BlogDetailPage(
        state = state,
        currentTab = currentTab,
        onTabClick = onTabClick,
        onLoginClick = onLoginClick,
        onCartClick = onCartClick,
        onBackToBlog = onBackToBlog,
        onRelatedClick = onRelatedClick,
        onContactClick = onContactClick
    )
}

@Composable
internal fun BlogDetailPage(
    state: BlogDetailViewModel.UiState,
    currentTab: String,
    onTabClick: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCartClick: () -> Unit,
    onBackToBlog: () -> Unit,
    onRelatedClick: (String) -> Unit,
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
                BlogDetailLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            state.notFound -> {
                BlogDetailNotFound(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onBackToBlog = onBackToBlog
                )
            }

            state.detail != null -> {
                BlogDetailContent(
                    detail = state.detail,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onBackToBlog = onBackToBlog,
                    onRelatedClick = onRelatedClick,
                    onContactClick = onContactClick
                )
            }
        }
    }
}

@Composable
private fun BlogDetailLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(12.dp))
        Text("Preparando la lectura...")
    }
}

@Composable
private fun BlogDetailNotFound(
    modifier: Modifier = Modifier,
    onBackToBlog: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No encontramos este articulo",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Es posible que haya cambiado de lugar. Vuelve al listado para seguir leyendo.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBackToBlog) {
            Text("Ir al blog")
        }
    }
}

@Composable
private fun BlogDetailContent(
    detail: BlogDetail,
    modifier: Modifier = Modifier,
    onBackToBlog: () -> Unit,
    onRelatedClick: (String) -> Unit,
    onContactClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            TextButton(onClick = onBackToBlog) {
                Text("Volver al blog")
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = detail.category.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = detail.title,
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = detail.subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                BlogDetailMetaRow(
                    publishDate = detail.publishDate,
                    readingTime = detail.readingTime
                )
            }
        }

        item {
            Image(
                painter = painterResource(id = DrawableCatalog.resolve(detail.heroImage)),
                contentDescription = detail.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )
        }

        if (detail.introParagraphs.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    detail.introParagraphs.forEach { paragraph ->
                        Text(
                            text = paragraph,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        if (detail.highlights.isNotEmpty()) {
            item {
                BlogDetailHighlights(highlights = detail.highlights)
            }
        }

        if (detail.sections.isNotEmpty()) {
            items(detail.sections) { section ->
                BlogDetailSectionCard(section = section)
            }
        }

        if (detail.tips.isNotEmpty()) {
            item {
                BlogDetailTips(tips = detail.tips)
            }
        }

        item {
            BlogDetailConclusion(
                title = detail.conclusionTitle,
                body = detail.conclusionBody,
                cta = detail.closingCta,
                onContactClick = onContactClick
            )
        }

        if (detail.relatedPosts.isNotEmpty()) {
            item {
                BlogDetailRelatedSection(
                    related = detail.relatedPosts,
                    onRelatedClick = onRelatedClick
                )
            }
        }

        item { MainFooter() }
    }
}

@Composable
private fun BlogDetailMetaRow(
    publishDate: String,
    readingTime: String
) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = publishDate,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "-",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = readingTime,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun BlogDetailHighlights(highlights: List<BlogDetailHighlight>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Destacados",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            highlights.forEach { highlight ->
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
                            text = highlight.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = highlight.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BlogDetailSectionCard(section: BlogDetailSection) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = section.heading,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            section.paragraphs.forEach { paragraph ->
                Text(
                    text = paragraph,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (section.bulletPoints.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    section.bulletPoints.forEach { bullet ->
                        Text(
                            text = "- $bullet",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BlogDetailTips(tips: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Consejos finales",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            tips.forEach { tip ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = tip,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun BlogDetailConclusion(
    title: String,
    body: String,
    cta: String,
    onContactClick: () -> Unit
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
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium
            )
            Button(
                onClick = onContactClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(cta, color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    }
}

@Composable
private fun BlogDetailRelatedSection(
    related: List<BlogDetailRelated>,
    onRelatedClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Tambien te puede interesar",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            related.forEach { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Image(
                                painter = painterResource(id = DrawableCatalog.resolve(item.imageResName)),
                                contentDescription = item.title,
                                modifier = Modifier.size(72.dp),
                                contentScale = ContentScale.Crop
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = item.excerpt,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Divider()
                        TextButton(onClick = { onRelatedClick(item.id) }) {
                            Text("Leer articulo")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBlogDetalle1Screen() {
    val detail = BlogDetailSeedData.details.getValue("blog-recetas-1")
    MilSaboresAppTheme {
        BlogDetalle1Screen(
            state = BlogDetailViewModel.UiState(isLoading = false, detail = detail),
            currentTab = "Blog",
            onTabClick = {},
            onLoginClick = {},
            onCartClick = {},
            onBackToBlog = {},
            onRelatedClick = {},
            onContactClick = {}
        )
    }
}
