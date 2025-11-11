package com.example.milsaboresapp.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.ProfileRegion
import com.example.milsaboresapp.domain.model.admin.AdminUserFormContent
import com.example.milsaboresapp.domain.model.admin.AdminUserItem
import com.example.milsaboresapp.domain.repository.AdminUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminUserFormViewModel(
    private val userRepository: AdminUserRepository
) : ViewModel() {

    data class FormErrors(
        val run: String? = null,
        val firstName: String? = null,
        val lastName: String? = null,
        val email: String? = null,
        val role: String? = null,
        val region: String? = null,
        val commune: String? = null
    )

    data class UiState(
        val isLoading: Boolean = true,
        val content: AdminUserFormContent? = null,
        val run: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val role: String = "",
        val region: String = "",
        val commune: String = "",
        val availableCommunes: List<String> = emptyList(),
        val errors: FormErrors = FormErrors(),
        val isSubmitting: Boolean = false,
        val submitSuccess: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.observeUserForm().collect { content ->
                _uiState.update { current ->
                    val initialRole = if (current.role.isNotBlank()) current.role else content.roles.firstOrNull().orEmpty()
                    val initialRegion = if (current.region.isNotBlank()) current.region else content.regions.firstOrNull()?.name.orEmpty()
                    val communes = findCommunes(initialRegion, content.regions)
                    val initialCommune = if (current.commune.isNotBlank() && communes.contains(current.commune)) {
                        current.commune
                    } else {
                        communes.firstOrNull().orEmpty()
                    }
                    current.copy(
                        isLoading = false,
                        content = content,
                        role = initialRole,
                        region = initialRegion,
                        availableCommunes = communes,
                        commune = initialCommune
                    )
                }
            }
        }
    }

    fun onRunChange(value: String) {
        _uiState.update {
            it.copy(run = value.uppercase(), errors = it.errors.copy(run = null))
        }
    }

    fun onFirstNameChange(value: String) {
        _uiState.update {
            it.copy(firstName = value, errors = it.errors.copy(firstName = null))
        }
    }

    fun onLastNameChange(value: String) {
        _uiState.update {
            it.copy(lastName = value, errors = it.errors.copy(lastName = null))
        }
    }

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(email = value, errors = it.errors.copy(email = null))
        }
    }

    fun onRoleChange(value: String) {
        _uiState.update {
            it.copy(role = value, errors = it.errors.copy(role = null))
        }
    }

    fun onRegionChange(value: String) {
        val content = _uiState.value.content ?: return
        val communes = findCommunes(value, content.regions)
        val commune = communes.firstOrNull().orEmpty()
        _uiState.update {
            it.copy(
                region = value,
                availableCommunes = communes,
                commune = commune,
                errors = it.errors.copy(region = null, commune = null)
            )
        }
    }

    fun onCommuneChange(value: String) {
        _uiState.update {
            it.copy(commune = value, errors = it.errors.copy(commune = null))
        }
    }

    fun submit() {
        val state = _uiState.value
        val content = state.content ?: return

        val runError = if (state.run.isBlank()) "Ingresa el RUN" else null
        val firstNameError = if (state.firstName.isBlank()) "Ingresa nombres" else null
        val lastNameError = if (state.lastName.isBlank()) "Ingresa apellidos" else null
        val emailError = if (!state.email.contains("@")) "Correo invalido" else null
        val roleError = if (state.role.isBlank()) "Selecciona un rol" else null
        val regionError = if (state.region.isBlank()) "Selecciona una region" else null
        val communeError = if (state.commune.isBlank()) "Selecciona una comuna" else null

        val hasErrors = listOf(runError, firstNameError, lastNameError, emailError, roleError, regionError, communeError).any { it != null }
        if (hasErrors) {
            _uiState.update {
                it.copy(
                    errors = FormErrors(
                        run = runError,
                        firstName = firstNameError,
                        lastName = lastNameError,
                        email = emailError,
                        role = roleError,
                        region = regionError,
                        commune = communeError
                    )
                )
            }
            return
        }

        if (state.isSubmitting) return

        _uiState.update {
            it.copy(isSubmitting = true)
        }

        viewModelScope.launch {
            val newUser = AdminUserItem(
                run = state.run.trim(),
                firstName = state.firstName.trim(),
                lastName = state.lastName.trim(),
                email = state.email.trim(),
                role = state.role,
                region = state.region,
                commune = state.commune
            )
            userRepository.addUser(newUser)
            _uiState.update {
                it.copy(
                    run = "",
                    firstName = "",
                    lastName = "",
                    email = "",
                    isSubmitting = false,
                    submitSuccess = true,
                    errors = FormErrors()
                )
            }
            // reset dependent selections after submission using latest content snapshot
            val latest = _uiState.value
            val latestContent = latest.content ?: content
            val defaultRole = latestContent.roles.firstOrNull().orEmpty()
            val defaultRegion = latestContent.regions.firstOrNull()?.name.orEmpty()
            val defaultCommunes = findCommunes(defaultRegion, latestContent.regions)
            val defaultCommune = defaultCommunes.firstOrNull().orEmpty()
            _uiState.update {
                it.copy(
                    role = defaultRole,
                    region = defaultRegion,
                    availableCommunes = defaultCommunes,
                    commune = defaultCommune
                )
            }
        }
    }

    fun resetSuccess() {
        _uiState.update {
            it.copy(submitSuccess = false)
        }
    }

    private fun findCommunes(region: String, regions: List<ProfileRegion>): List<String> {
        if (region.isBlank()) return emptyList()
        return regions.firstOrNull { it.name.equals(region, ignoreCase = true) }?.communes.orEmpty()
    }

    companion object {
        fun provideFactory(
            userRepository: AdminUserRepository
        ) = viewModelFactory {
            initializer {
                AdminUserFormViewModel(userRepository)
            }
        }
    }
}
