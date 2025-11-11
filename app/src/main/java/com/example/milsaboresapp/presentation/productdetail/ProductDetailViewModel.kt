package com.example.milsaboresapp.presentation.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.Producto
import com.example.milsaboresapp.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productId: String,
    private val productoRepository: ProductoRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val product: Producto? = null,
        val related: List<Producto> = emptyList(),
        val quantity: Int = 1,
        val customMessage: String = "",
        val showCustomMessageField: Boolean = true   // 👈 ahora por defecto SIEMPRE visible
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            productoRepository.observeProductos()
                .map { productos ->
                    val product = productos.find { it.id == productId }
                    val related = product?.let {
                        productos.filter { other ->
                            other.id != product.id && other.categoria == product.categoria
                        }.take(4)
                    } ?: emptyList()

                    UiState(
                        isLoading = false,
                        product = product,
                        related = related,
                        quantity = _uiState.value.quantity,
                        customMessage = _uiState.value.customMessage,
                        // si algún día quieres limitar solo a tortas,
                        // aquí podríamos volver a poner una condición.
                        showCustomMessageField = true
                    )
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    fun onQuantityChange(value: Int) {
        val sanitized = value.coerceAtLeast(1).coerceAtMost(99)
        _uiState.value = _uiState.value.copy(quantity = sanitized)
    }

    fun onCustomMessageChange(value: String) {
        _uiState.value = _uiState.value.copy(customMessage = value.take(50))
    }

    companion object {
        fun provideFactory(
            productId: String,
            productoRepository: ProductoRepository
        ) = viewModelFactory {
            initializer {
                ProductDetailViewModel(
                    productId = productId,
                    productoRepository = productoRepository
                )
            }
        }
    }
}
