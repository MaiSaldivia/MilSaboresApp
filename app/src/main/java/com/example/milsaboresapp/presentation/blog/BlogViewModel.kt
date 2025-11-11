package com.example.milsaboresapp.presentation.blog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.BlogPost
import com.example.milsaboresapp.domain.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class BlogViewModel(
    private val blogRepository: BlogRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val query: String = "",
        val selectedCategory: String? = null,
        val categories: List<String> = emptyList(),
        val posts: List<BlogPost> = emptyList()
    ) {
        val hasResults: Boolean get() = posts.isNotEmpty()
    }

    private val query = MutableStateFlow("")
    private val selectedCategory = MutableStateFlow<String?>(null)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                blogRepository.observePosts(),
                blogRepository.observeCategories(),
                query,
                selectedCategory
            ) { posts, categories, q, category ->
                val filtered = posts.filter { post ->
                    val matchesQuery = if (q.isBlank()) {
                        true
                    } else {
                        post.title.contains(q, ignoreCase = true) ||
                            post.excerpt.contains(q, ignoreCase = true) ||
                            post.category.contains(q, ignoreCase = true)
                    }
                    val matchesCategory = category.isNullOrBlank() || post.category.equals(category, ignoreCase = true)
                    matchesQuery && matchesCategory
                }

                UiState(
                    isLoading = false,
                    query = q,
                    selectedCategory = category,
                    categories = categories,
                    posts = filtered
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun onCategorySelected(value: String?) {
        selectedCategory.value = value
    }

    fun clearFilters() {
        query.value = ""
        selectedCategory.value = null
    }

    companion object {
        fun provideFactory(
            blogRepository: BlogRepository
        ) = viewModelFactory {
            initializer {
                BlogViewModel(blogRepository)
            }
        }
    }
}
