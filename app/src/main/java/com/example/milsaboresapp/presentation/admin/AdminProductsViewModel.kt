package com.example.milsaboresapp.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.admin.AdminProductItem
import com.example.milsaboresapp.domain.repository.AdminProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminProductsViewModel(
    private val productRepository: AdminProductRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val query: String = "",
        val selectedCategory: String? = null,
        val categories: List<String> = emptyList(),
        val products: List<AdminProductItem> = emptyList(),
        val deleteSuccess: Boolean = false
    ) {
        val hasProducts: Boolean get() = products.isNotEmpty()
    }

    private val query = MutableStateFlow("")
    private val selectedCategory = MutableStateFlow<String?>(null)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                productRepository.observeProducts(),
                productRepository.observeProductForm(),
                query,
                selectedCategory
            ) { products, form, q, category ->
                val filtered = products.filter { item ->
                    val matchesQuery = q.isBlank() || item.code.contains(q, ignoreCase = true) || item.name.contains(q, ignoreCase = true)
                    val matchesCategory = category.isNullOrBlank() || item.category.equals(category, ignoreCase = true)
                    matchesQuery && matchesCategory
                }
                Triple(filtered, form.categories, Pair(q, category))
            }.collect { (filtered, categories, values) ->
                val (currentQuery, currentCategory) = values
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        query = currentQuery,
                        selectedCategory = currentCategory,
                        categories = categories,
                        products = filtered
                    )
                }
            }
        }
    }

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun onCategoryChange(value: String?) {
        selectedCategory.value = value
    }

    fun deleteProduct(code: String) {
        viewModelScope.launch {
            productRepository.deleteProduct(code)
            _uiState.update {
                it.copy(deleteSuccess = true)
            }
        }
    }

    fun resetDeleteSuccess() {
        _uiState.update {
            it.copy(deleteSuccess = false)
        }
    }

    companion object {
        fun provideFactory(
            productRepository: AdminProductRepository
        ) = viewModelFactory {
            initializer {
                AdminProductsViewModel(productRepository)
            }
        }
    }
}
