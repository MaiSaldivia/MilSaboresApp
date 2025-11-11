package com.example.milsaboresapp.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.admin.AdminLoginContent
import com.example.milsaboresapp.domain.repository.AdminAuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminLoginViewModel(
    private val authRepository: AdminAuthRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val content: AdminLoginContent? = null,
        val email: String = "",
        val password: String = "",
        val rememberSession: Boolean = false,
        val isSubmitting: Boolean = false,
        val submitSuccess: Boolean = false,
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.observeLoginContent().collect { content ->
                _uiState.update {
                    it.copy(isLoading = false, content = content)
                }
            }
        }
    }

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(email = value, errorMessage = null)
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(password = value, errorMessage = null)
        }
    }

    fun onRememberChange(value: Boolean) {
        _uiState.update {
            it.copy(rememberSession = value)
        }
    }

    fun submit() {
        val current = _uiState.value
        if (current.email.isBlank() || current.password.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Completa tus credenciales para continuar.")
            }
            return
        }
        if (current.isSubmitting) return
        _uiState.update {
            it.copy(isSubmitting = true, errorMessage = null)
        }
        viewModelScope.launch {
            delay(900)
            _uiState.update {
                it.copy(
                    isSubmitting = false,
                    submitSuccess = true,
                    password = if (it.rememberSession) it.password else ""
                )
            }
        }
    }

    fun resetSuccess() {
        _uiState.update {
            it.copy(submitSuccess = false)
        }
    }

    companion object {
        fun provideFactory(
            authRepository: AdminAuthRepository
        ) = viewModelFactory {
            initializer {
                AdminLoginViewModel(authRepository)
            }
        }
    }
}
