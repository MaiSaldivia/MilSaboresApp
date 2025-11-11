package com.example.milsaboresapp.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.admin.AdminDashboard
import com.example.milsaboresapp.domain.repository.AdminDashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminDashboardViewModel(
    private val dashboardRepository: AdminDashboardRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val dashboard: AdminDashboard? = null
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
            dashboardRepository: AdminDashboardRepository
        ) = viewModelFactory {
            initializer {
                AdminDashboardViewModel(dashboardRepository)
            }
        }
    }
}
