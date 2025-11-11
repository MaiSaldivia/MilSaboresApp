// presentation/cart/CartViewModel.kt
package com.example.milsaboresapp.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.CartInfo
import com.example.milsaboresapp.domain.model.Producto
import com.example.milsaboresapp.domain.model.ShippingOption
import com.example.milsaboresapp.domain.repository.CartRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val cartInfo: CartInfo? = null,
        val isCheckoutProcessing: Boolean = false,
        val checkoutSuccess: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            cartRepository.observeCart().collect { info ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    cartInfo = info
                )
            }
        }
    }

    // Añadir producto al carrito
    fun addProduct(
        product: Producto,
        quantity: Int = 1,
        message: String? = null
    ) {
        viewModelScope.launch {
            cartRepository.addItem(product, quantity, message)
        }
    }

    fun increment(itemId: String) {
        val item = _uiState.value.cartInfo?.items?.firstOrNull { it.id == itemId } ?: return
        setQuantity(itemId, item.quantity + 1)
    }

    fun decrement(itemId: String) {
        val item = _uiState.value.cartInfo?.items?.firstOrNull { it.id == itemId } ?: return
        setQuantity(itemId, item.quantity - 1)
    }

    fun remove(itemId: String) {
        viewModelScope.launch {
            cartRepository.removeItem(itemId)
        }
    }

    private fun setQuantity(itemId: String, quantity: Int) {
        viewModelScope.launch {
            cartRepository.setQuantity(itemId, quantity)
        }
    }

    // 🆕 seleccionar tipo de envío
    fun selectShippingOption(option: ShippingOption) {
        viewModelScope.launch {
            cartRepository.setShippingOption(option)
        }
    }

    // Procesar checkout y mostrar diálogo de éxito
    fun checkout() {
        val info = _uiState.value.cartInfo ?: return
        if (info.items.isEmpty() || _uiState.value.isCheckoutProcessing) {
            return
        }

        _uiState.value = _uiState.value.copy(isCheckoutProcessing = true)

        viewModelScope.launch {
            // Simula un pequeño tiempo de procesamiento
            delay(800L)

            // Vaciar el carrito
            cartRepository.clearCart()

            // Mostrar el diálogo de éxito
            _uiState.value = _uiState.value.copy(
                isCheckoutProcessing = false,
                checkoutSuccess = true
            )
        }
    }

    fun resetCheckoutSuccess() {
        _uiState.value = _uiState.value.copy(checkoutSuccess = false)
    }

    companion object {
        fun provideFactory(
            cartRepository: CartRepository
        ) = viewModelFactory {
            initializer {
                CartViewModel(cartRepository)
            }
        }
    }
}
