package com.example.milsaboresapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.RegisterContent
import com.example.milsaboresapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estas funciones vienen de AuthValidators.kt en este mismo package
import com.example.milsaboresapp.presentation.auth.validateEmailForAuth
import com.example.milsaboresapp.presentation.auth.validatePasswordForAuth

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val content: RegisterContent? = null,

        // ---- Campos del formulario ----
        val run: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val phone: String = "",
        val birthDate: String = "",
        val region: String = "",
        val commune: String = "",
        val address: String = "",
        val promoCode: String = "",          // 👈 código FELICES50 (opcional)
        val password: String = "",
        val confirmPassword: String = "",
        val acceptsPromotions: Boolean = true,

        // ---- Estado de envío ----
        val isSubmitting: Boolean = false,
        val submitSuccess: Boolean = false,
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.observeRegisterContent().collect { content ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    content = content
                )
            }
        }
    }

    // ----------- Handlers de cambios de campos ------------

    fun onRunChange(value: String) {
        _uiState.value = _uiState.value.copy(run = value, errorMessage = null)
    }

    fun onFirstNameChange(value: String) {
        _uiState.value = _uiState.value.copy(firstName = value, errorMessage = null)
    }

    fun onLastNameChange(value: String) {
        _uiState.value = _uiState.value.copy(lastName = value, errorMessage = null)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, errorMessage = null)
    }

    fun onPhoneChange(value: String) {
        _uiState.value = _uiState.value.copy(phone = value, errorMessage = null)
    }

    fun onBirthDateChange(value: String) {
        _uiState.value = _uiState.value.copy(birthDate = value, errorMessage = null)
    }

    fun onRegionChange(value: String) {
        _uiState.value = _uiState.value.copy(region = value, commune = "", errorMessage = null)
    }

    fun onCommuneChange(value: String) {
        _uiState.value = _uiState.value.copy(commune = value, errorMessage = null)
    }

    fun onAddressChange(value: String) {
        _uiState.value = _uiState.value.copy(address = value, errorMessage = null)
    }

    fun onPromoCodeChange(value: String) {
        _uiState.value = _uiState.value.copy(promoCode = value, errorMessage = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = value, errorMessage = null)
    }

    fun onAcceptsPromotionsChange(value: Boolean) {
        _uiState.value = _uiState.value.copy(acceptsPromotions = value)
    }

    // -------------------- SUBMIT -------------------------

    fun submit() {
        val current = _uiState.value

        val promoCodeClean = current.promoCode.trim().uppercase()

        // RUN
        val runError = validateRun(current.run)
        if (runError != null) {
            _uiState.value = current.copy(errorMessage = runError)
            return
        }

        // Nombre
        val first = current.firstName.trim()
        if (first.isEmpty()) {
            _uiState.value = current.copy(errorMessage = "El nombre es obligatorio.")
            return
        }
        if (first.length > 50) {
            _uiState.value = current.copy(
                errorMessage = "El nombre no puede superar los 50 caracteres."
            )
            return
        }

        // Apellidos
        val last = current.lastName.trim()
        if (last.isEmpty()) {
            _uiState.value = current.copy(errorMessage = "Los apellidos son obligatorios.")
            return
        }
        if (last.length > 100) {
            _uiState.value = current.copy(
                errorMessage = "Los apellidos no pueden superar los 100 caracteres."
            )
            return
        }

        // Correo
        val emailError = validateEmailForAuth(current.email)
        if (emailError != null) {
            _uiState.value = current.copy(errorMessage = emailError)
            return
        }

        // Código promocional: solo vacío o FELICES50
        if (promoCodeClean.isNotBlank() && promoCodeClean != "FELICES50") {
            _uiState.value = current.copy(
                errorMessage = "El código promocional ingresado no es válido."
            )
            return
        }

        // Fecha de nacimiento
        val birth = current.birthDate.trim()
        if (birth.isEmpty()) {
            _uiState.value = current.copy(errorMessage = "La fecha de nacimiento es obligatoria.")
            return
        }

        // Región
        if (current.region.isBlank()) {
            _uiState.value = current.copy(errorMessage = "Debes seleccionar una región.")
            return
        }

        // Comuna
        if (current.commune.isBlank()) {
            _uiState.value = current.copy(errorMessage = "Debes seleccionar una comuna.")
            return
        }

        // Dirección
        val address = current.address.trim()
        if (address.isEmpty()) {
            _uiState.value = current.copy(errorMessage = "La dirección es obligatoria.")
            return
        }
        if (address.length > 300) {
            _uiState.value = current.copy(
                errorMessage = "La dirección no puede superar los 300 caracteres."
            )
            return
        }

        // Contraseña
        val passwordError = validatePasswordForAuth(current.password)
        if (passwordError != null) {
            _uiState.value = current.copy(errorMessage = passwordError)
            return
        }

        // Confirmar contraseña
        if (current.confirmPassword.isBlank()) {
            _uiState.value = current.copy(errorMessage = "Debes confirmar la contraseña.")
            return
        }
        if (current.password != current.confirmPassword) {
            _uiState.value = current.copy(errorMessage = "Las contraseñas no coinciden.")
            return
        }

        // Si todo OK -> llamamos al repositorio (Room)
        _uiState.value = current.copy(isSubmitting = true, errorMessage = null)

        viewModelScope.launch {
            val run = current.run.trim()
            val firstName = current.firstName.trim()
            val lastName = current.lastName.trim()
            val email = current.email.trim()
            val phone = current.phone.trim()
            val birthDate = current.birthDate.trim()
            val region = current.region
            val commune = current.commune
            val addressFinal = current.address.trim()
            val acceptsPromos = current.acceptsPromotions
            val promoCodeToSave = promoCodeClean.ifBlank { null }

            val errorFromRepo = authRepository.registerUser(
                run = run,
                firstName = firstName,
                lastName = lastName,
                email = email,
                phone = phone,
                birthDate = birthDate,
                region = region,
                commune = commune,
                address = addressFinal,
                password = current.password,
                promoCode = promoCodeToSave,
                acceptsPromotions = acceptsPromos
            )

            if (errorFromRepo == null) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    submitSuccess = true,
                    password = "",
                    confirmPassword = ""
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    errorMessage = errorFromRepo
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

    // ------------------- Validadores privados -------------------

    private fun validateRun(raw: String): String? {
        val run = raw.trim().uppercase()

        if (run.isEmpty()) {
            return "El RUN es obligatorio."
        }

        if (run.length !in 7..9) {
            return "El RUN debe tener entre 7 y 9 caracteres (sin puntos ni guión)."
        }

        if (!run.matches(Regex("^[0-9]{6,8}[0-9K]\$"))) {
            return "Formato de RUN inválido. Ej: 19011022K (sin puntos ni guión)."
        }

        val body = run.dropLast(1)
        val dv = run.last()

        var factor = 2
        var sum = 0
        for (digitChar in body.reversed()) {
            val digit = digitChar.digitToInt()
            sum += digit * factor
            factor++
            if (factor > 7) factor = 2
        }

        val remainder = 11 - (sum % 11)
        val dvExpected = when (remainder) {
            11 -> '0'
            10 -> 'K'
            else -> remainder.toString()[0]
        }

        if (dv != dvExpected) {
            return "El RUN no es válido."
        }

        return null
    }

    companion object {
        fun provideFactory(
            authRepository: AuthRepository
        ) = viewModelFactory {
            initializer {
                RegisterViewModel(authRepository)
            }
        }
    }
}
