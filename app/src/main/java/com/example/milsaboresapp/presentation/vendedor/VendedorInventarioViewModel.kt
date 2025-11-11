package com.example.milsaboresapp.presentation.vendedor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.vendedor.VendedorInventarioItem
import com.example.milsaboresapp.domain.repository.vendedor.VendedorInventarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VendedorInventarioViewModel(
    private val inventarioRepository: VendedorInventarioRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val inventario: List<VendedorInventarioItem> = emptyList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            inventarioRepository.observeInventario().collect { lista ->
                _uiState.update {
                    it.copy(isLoading = false, inventario = lista)
                }
            }
        }
    }

    companion object {
        fun provideFactory(
            inventarioRepository: VendedorInventarioRepository
        ) = viewModelFactory {
            initializer {
                VendedorInventarioViewModel(inventarioRepository)
            }
        }
    }
}
