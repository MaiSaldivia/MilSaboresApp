package com.example.milsaboresapp.presentation.vendedor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.vendedor.VendedorDashboard
import com.example.milsaboresapp.domain.repository.vendedor.VendedorDashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VendedorDashboardViewModel(
    private val dashboardRepository: VendedorDashboardRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val dashboard: VendedorDashboard? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dashboardRepository.observeDashboard().collect { dashboard ->
                _uiState.update {
                    it.copy(isLoading = false, dashboard = dashboard)
                }
            }
        }
    }

    companion object {
        fun provideFactory(
            dashboardRepository: VendedorDashboardRepository
        ) = viewModelFactory {
            initializer {
                VendedorDashboardViewModel(dashboardRepository)
            }
        }
    }
}
