package com.example.milsaboresapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.BlogPost
import com.example.milsaboresapp.domain.model.Producto
import com.example.milsaboresapp.domain.repository.BlogRepository
import com.example.milsaboresapp.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    private val productoRepository: ProductoRepository,
    private val blogRepository: BlogRepository
) : ViewModel() {

    data class HomeUiState(
        val hero: Hero = Hero(),
        val destacados: List<Producto> = emptyList(),
        val blogDestacados: List<BlogPost> = emptyList(),
        val isLoading: Boolean = true
    ) {
        data class Hero(
            val imageResName: String = "vitrina1",
            val title: String = "Sabor artesanal en cada bocado",
            val ctaLabel: String = "Ver Productos"
        )
    }

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                productoRepository.observeDestacados(limit = 6),
                blogRepository.observeHighlights(limit = 4)
            ) { productos, blogs ->
                HomeUiState(
                    hero = HomeUiState.Hero(),
                    destacados = productos,
                    blogDestacados = blogs,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    companion object {
        fun provideFactory(
            productoRepository: ProductoRepository,
            blogRepository: BlogRepository
        ) = viewModelFactory {
            initializer {
                HomeViewModel(
                    productoRepository = productoRepository,
                    blogRepository = blogRepository
                )
            }
        }
    }
}
