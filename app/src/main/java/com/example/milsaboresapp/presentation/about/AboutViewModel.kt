package com.example.milsaboresapp.presentation.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.AboutInfo
import com.example.milsaboresapp.domain.repository.AboutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AboutViewModel(
    private val aboutRepository: AboutRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val info: AboutInfo? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            aboutRepository.observeAboutInfo().collect { aboutInfo ->
                _uiState.value = UiState(
                    isLoading = false,
                    info = aboutInfo
                )
            }
        }
    }

    companion object {
        fun provideFactory(
            aboutRepository: AboutRepository
        ) = viewModelFactory {
            initializer {
                AboutViewModel(aboutRepository)
            }
        }
    }
}
