package com.example.milsaboresapp.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.Producto
import com.example.milsaboresapp.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val productoRepository: ProductoRepository
) : ViewModel() {

    enum class SortOption { Relevance, PriceAsc, PriceDesc, NameAsc }

    data class UiState(
        val isLoading: Boolean = true,
        val query: String = "",
        val selectedCategory: String? = null,
        val sort: SortOption = SortOption.Relevance,
        val categories: List<String> = emptyList(),
        val products: List<Producto> = emptyList()
    ) {
        val hasResults: Boolean get() = products.isNotEmpty()
    }

    private val query = MutableStateFlow("")
    private val selectedCategory = MutableStateFlow<String?>(null)
    private val sortOption = MutableStateFlow(SortOption.Relevance)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                productoRepository.observeProductos(),
                productoRepository.observeCategorias(),
                query,
                selectedCategory,
                sortOption
            ) { productos, categorias, q, cat, sort ->
                val filtered = productos.filter { producto ->
                    val matchesQuery = if (q.isBlank()) true else producto.nombre.contains(q, ignoreCase = true) || producto.attr.contains(q, ignoreCase = true) || producto.categoria.contains(q, ignoreCase = true)
                    val matchesCategory = cat.isNullOrBlank() || producto.categoria.equals(cat, ignoreCase = true)
                    matchesQuery && matchesCategory
                }.let { list ->
                    when (sort) {
                        SortOption.Relevance -> list
                        SortOption.PriceAsc -> list.sortedBy { it.precio }
                        SortOption.PriceDesc -> list.sortedByDescending { it.precio }
                        SortOption.NameAsc -> list.sortedBy { it.nombre }
                    }
                }

                UiState(
                    isLoading = false,
                    query = q,
                    selectedCategory = cat,
                    sort = sort,
                    categories = categorias,
                    products = filtered
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

    fun onSortSelected(value: SortOption) {
        sortOption.value = value
    }

    fun clearFilters() {
        query.value = ""
        selectedCategory.value = null
        sortOption.value = SortOption.Relevance
    }

    companion object {
        fun provideFactory(
            productoRepository: ProductoRepository
        ) = viewModelFactory {
            initializer {
                ProductsViewModel(productoRepository)
            }
        }
    }
}
