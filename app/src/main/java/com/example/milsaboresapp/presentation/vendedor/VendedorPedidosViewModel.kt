package com.example.milsaboresapp.presentation.vendedor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.vendedor.VendedorPedidoItem
import com.example.milsaboresapp.domain.repository.vendedor.VendedorPedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VendedorPedidosViewModel(
    private val pedidoRepository: VendedorPedidoRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val pedidos: List<VendedorPedidoItem> = emptyList(),
        val selectedPedido: VendedorPedidoItem? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            pedidoRepository.observePedidos().collect { pedidos ->
                _uiState.update {
                    it.copy(isLoading = false, pedidos = pedidos)
                }
            }
        }
    }

    fun onPedidoSelected(id: String) {
        val pedido = _uiState.value.pedidos.firstOrNull { it.id == id }
        _uiState.update {
            it.copy(selectedPedido = pedido)
        }
    }

    fun onDismissDetalle() {
        _uiState.update {
            it.copy(selectedPedido = null)
        }
    }

    companion object {
        fun provideFactory(
            pedidoRepository: VendedorPedidoRepository
        ) = viewModelFactory {
            initializer {
                VendedorPedidosViewModel(pedidoRepository)
            }
        }
    }
}
