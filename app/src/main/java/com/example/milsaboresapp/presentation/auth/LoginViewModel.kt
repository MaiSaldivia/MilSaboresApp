package com.example.milsaboresapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.LoginContent
import com.example.milsaboresapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val content: LoginContent? = null,
        val email: String = "",
        val password: String = "",
        val remember: Boolean = false,
        val isSubmitting: Boolean = false,
        val submitSuccess: Boolean = false,
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.observeLoginContent().collect { content ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    content = content
                )
            }
        }
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, errorMessage = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun onRememberChange(value: Boolean) {
        _uiState.value = _uiState.value.copy(remember = value)
    }

    fun submit() {
        val current = _uiState.value

        // Validaciones según enunciado (mismas funciones que en registro)
        val emailError = validateEmailForAuth(current.email)
        if (emailError != null) {
            _uiState.value = current.copy(errorMessage = emailError)
            return
        }

        val passwordError = validatePasswordForAuth(current.password)
        if (passwordError != null) {
            _uiState.value = current.copy(errorMessage = passwordError)
            return
        }

        _uiState.value = current.copy(isSubmitting = true, errorMessage = null)

        viewModelScope.launch {
            try {
                // login devuelve User? (null si falla)
                val user = authRepository.login(
                    email = current.email.trim(),
                    password = current.password
                )

                if (user != null) {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        submitSuccess = true,
                        // si no marcó "Recordarme", limpiamos la contraseña
                        password = if (current.remember) current.password else ""
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        errorMessage = "Correo o contraseña incorrectos."
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    errorMessage = e.message ?: "Ocurrió un error al iniciar sesión."
                )
            }
        }
    }

    fun resetSuccess() {
        _uiState.value = _uiState.value.copy(
            submitSuccess = false,
            errorMessage = null
        )
    }

    companion object {
        fun provideFactory(
            authRepository: AuthRepository
        ) = viewModelFactory {
            initializer {
                LoginViewModel(authRepository)
            }
        }
    }
}
