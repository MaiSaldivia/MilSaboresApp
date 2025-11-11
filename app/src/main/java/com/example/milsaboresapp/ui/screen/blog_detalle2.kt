package com.example.milsaboresapp.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.milsaboresapp.data.local.datasource.BlogDetailSeedData
import com.example.milsaboresapp.presentation.blogdetail.BlogDetailViewModel
import com.example.milsaboresapp.ui.theme.MilSaboresAppTheme

@Composable
fun BlogDetalle2Screen(
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

@Preview(showBackground = true)
@Composable
private fun PreviewBlogDetalle2Screen() {
    val detail = BlogDetailSeedData.details.getValue("blog-tips-1")
    MilSaboresAppTheme {
        BlogDetalle2Screen(
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
