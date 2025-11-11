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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsaboresapp.domain.model.BlogPost
import com.example.milsaboresapp.presentation.blog.BlogViewModel
import com.example.milsaboresapp.ui.common.MainFooter
import com.example.milsaboresapp.ui.common.MainNavigationBar
import com.example.milsaboresapp.ui.common.MainTopAppBar
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme
import com.example.milsaboresapp.ui.util.DrawableCatalog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogScreen(
    state: BlogViewModel.UiState,
    currentTab: String,
    onTabClick: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCartClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onPostClick: (String) -> Unit
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Blog Mil Sabores",
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 32.sp
                    )
                    Text(
                        text = "Historias, tips y recetas para endulzar tus días.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                BlogSearchBar(
                    query = state.query,
                    onQueryChange = onQueryChange
                )
            }

            if (state.categories.isNotEmpty()) {
                item {
                    BlogCategoriesChips(
                        categories = state.categories,
                        selected = state.selectedCategory,
                        onChipClick = onCategorySelected
                    )
                }
            }

            when {
                state.isLoading -> {
                    item { BlogLoading() }
                }

                state.posts.isEmpty() -> {
                    item { BlogEmptyState() }
                }

                else -> {
                    items(state.posts, key = { it.id }) { post ->
                        BlogPostListCard(
                            post = post,
                            onReadMore = { onPostClick(post.id) }
                        )
                    }
                }
            }

            item { MainFooter() }
        }
    }
}

@Composable
private fun BlogSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Buscar en el blog") },
        singleLine = true
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BlogCategoriesChips(
    categories: List<String>,
    selected: String?,
    onChipClick: (String?) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = { onChipClick(null) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                contentColor = if (selected == null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("Todas")
        }

        categories.forEach { category ->
            Button(
                onClick = { onChipClick(category) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected == category) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (selected == category) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(category)
            }
        }
    }
}

@Composable
private fun BlogPostListCard(
    post: BlogPost,
    onReadMore: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = DrawableCatalog.resolve(post.imageResName)),
                contentDescription = post.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = post.category.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = post.excerpt,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = onReadMore,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Leer más", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    }
}

@Composable
private fun BlogLoading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
    Text("Cargando entradas...")
    }
}

@Composable
private fun BlogEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No encontramos entradas con esos filtros.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Prueba otra búsqueda o categoría.")
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBlogScreen() {
    MilSaboresAppTheme {
        BlogScreen(
            state = BlogViewModel.UiState(
                isLoading = false,
                query = "",
                selectedCategory = null,
                categories = listOf("Recetas", "Tips"),
                posts = listOf(
                    BlogPost(
                        id = "blog-1",
                        category = "Recetas",
                        title = "Torta húmeda de chocolate",
                        excerpt = "Aprende los secretos detrás de nuestra receta más pedida.",
                        imageResName = "blog1"
                    )
                )
            ),
            currentTab = "Blog",
            onTabClick = {},
            onLoginClick = {},
            onCartClick = {},
            onQueryChange = {},
            onCategorySelected = {},
            onPostClick = {}
        )
    }
}
