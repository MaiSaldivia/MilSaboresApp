package com.example.milsaboresapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.data.local.dao.UserDao
import com.example.milsaboresapp.domain.model.ProfileContent
import com.example.milsaboresapp.domain.model.ProfilePersonalData
import com.example.milsaboresapp.domain.model.ProfilePreferences
import com.example.milsaboresapp.domain.repository.ProfileRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val userDao: UserDao            // 👈 ahora también recibimos Room
) : ViewModel() {

    data class PersonalForm(
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val phone: String = "",
        val birthDate: String = "",
        val address: String = "",
        val region: String = "",
        val commune: String = ""
    )

    data class PersonalErrors(
        val firstName: String? = null,
        val lastName: String? = null,
        val address: String? = null,
        val region: String? = null,
        val commune: String? = null
    )

    data class PreferenceForm(
        val shippingOptionId: String = "",
        val defaultCoupon: String = "",
        val newsletter: Boolean = false,
        val saveAddress: Boolean = false
    )

    data class PasswordForm(
        val current: String = "",
        val new: String = "",
        val confirm: String = ""
    )

    data class PasswordErrors(
        val current: String? = null,
        val new: String? = null,
        val confirm: String? = null
    )

    data class UiState(
        val isLoading: Boolean = true,
        val content: ProfileContent? = null,
        val personalForm: PersonalForm = PersonalForm(),
        val personalErrors: PersonalErrors = PersonalErrors(),
        val preferenceForm: PreferenceForm = PreferenceForm(),
        val availableCommunes: List<String> = emptyList(),
        val isSavingProfile: Boolean = false,
        val profileSuccess: Boolean = false,
        val passwordForm: PasswordForm = PasswordForm(),
        val passwordErrors: PasswordErrors = PasswordErrors(),
        val isUpdatingPassword: Boolean = false,
        val passwordSuccess: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadProfileFromSeedAndRoom()
    }

    /**
     * Lee ProfileSeedData desde el repositorio y lo mezcla
     * con el último usuario guardado en Room (UserDao).
     */
    private fun loadProfileFromSeedAndRoom() {
        viewModelScope.launch {
            profileRepository.observeProfileContent().collect { contentFromRepo ->
                // 1) Datos base del seed (Camila)
                val baseContent = contentFromRepo
                val basePersonal = baseContent.personalData

                // 2) Último usuario en Room (el último registrado / guardado)
                val lastUser = userDao.getLastUser()

                val personalFromDb: ProfilePersonalData? = lastUser?.let { u ->
                    ProfilePersonalData(
                        firstName = u.firstName.ifBlank { basePersonal.firstName },
                        lastName = u.lastName.ifBlank { basePersonal.lastName },
                        email = u.email,   // correo viene 100% de Room
                        phone = u.phone.ifBlank { basePersonal.phone },
                        birthDate = u.birthDate.ifBlank { basePersonal.birthDate },
                        address = u.address.ifBlank { basePersonal.address },
                        region = u.region.ifBlank { basePersonal.region },
                        commune = u.commune.ifBlank { basePersonal.commune }
                    )
                }

                val finalPersonal = personalFromDb ?: basePersonal

                val finalContent = baseContent.copy(
                    welcomeMessage = "Estas editando el perfil de ${finalPersonal.email}",
                    personalData = finalPersonal
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        content = finalContent,
                        personalForm = PersonalForm(
                            firstName = finalPersonal.firstName,
                            lastName = finalPersonal.lastName,
                            email = finalPersonal.email,
                            phone = finalPersonal.phone,
                            birthDate = finalPersonal.birthDate,
                            address = finalPersonal.address,
                            region = finalPersonal.region,
                            commune = finalPersonal.commune
                        ),
                        preferenceForm = PreferenceForm(
                            shippingOptionId = finalContent.preferences.shippingOptionId,
                            defaultCoupon = finalContent.preferences.defaultCoupon,
                            newsletter = finalContent.preferences.newsletter,
                            saveAddress = finalContent.preferences.saveAddress
                        ),
                        availableCommunes = findCommunes(finalPersonal.region, finalContent)
                    )
                }
            }
        }
    }

    /** Llamar cada vez que entres a la pantalla de Perfil. */
    fun refreshFromDatabase() {
        loadProfileFromSeedAndRoom()
    }

    // ------------------------------------------------------------------
    // Handlers de formulario (los mismos que ya tenías)
    // ------------------------------------------------------------------

    fun onFirstNameChange(value: String) {
        _uiState.update {
            it.copy(
                personalForm = it.personalForm.copy(firstName = value),
                personalErrors = it.personalErrors.copy(firstName = null)
            )
        }
    }

    fun onLastNameChange(value: String) {
        _uiState.update {
            it.copy(
                personalForm = it.personalForm.copy(lastName = value),
                personalErrors = it.personalErrors.copy(lastName = null)
            )
        }
    }

    fun onPhoneChange(value: String) {
        _uiState.update {
            it.copy(personalForm = it.personalForm.copy(phone = value))
        }
    }

    fun onBirthDateChange(value: String) {
        _uiState.update {
            it.copy(personalForm = it.personalForm.copy(birthDate = value))
        }
    }

    fun onAddressChange(value: String) {
        _uiState.update {
            it.copy(
                personalForm = it.personalForm.copy(address = value),
                personalErrors = it.personalErrors.copy(address = null)
            )
        }
    }

    fun onRegionChange(value: String) {
        val content = _uiState.value.content
        val communes = findCommunes(value, content)
        _uiState.update {
            it.copy(
                personalForm = it.personalForm.copy(region = value, commune = ""),
                availableCommunes = communes,
                personalErrors = it.personalErrors.copy(
                    region = null,
                    commune = null
                )
            )
        }
    }

    fun onCommuneChange(value: String) {
        _uiState.update {
            it.copy(
                personalForm = it.personalForm.copy(commune = value),
                personalErrors = it.personalErrors.copy(commune = null)
            )
        }
    }

    fun onShippingOptionChange(value: String) {
        _uiState.update {
            it.copy(preferenceForm = it.preferenceForm.copy(shippingOptionId = value))
        }
    }

    fun onDefaultCouponChange(value: String) {
        _uiState.update {
            it.copy(preferenceForm = it.preferenceForm.copy(defaultCoupon = value.uppercase()))
        }
    }

    fun onNewsletterChange(value: Boolean) {
        _uiState.update {
            it.copy(preferenceForm = it.preferenceForm.copy(newsletter = value))
        }
    }

    fun onSaveAddressChange(value: Boolean) {
        _uiState.update {
            it.copy(preferenceForm = it.preferenceForm.copy(saveAddress = value))
        }
    }

    fun onCurrentPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                passwordForm = it.passwordForm.copy(current = value),
                passwordErrors = it.passwordErrors.copy(current = null)
            )
        }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                passwordForm = it.passwordForm.copy(new = value),
                passwordErrors = it.passwordErrors.copy(new = null)
            )
        }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                passwordForm = it.passwordForm.copy(confirm = value),
                passwordErrors = it.passwordErrors.copy(confirm = null)
            )
        }
    }

    fun saveProfile() {
        val current = _uiState.value
        val content = current.content ?: return
        val form = current.personalForm
        val preferences = current.preferenceForm

        val firstNameError = when {
            form.firstName.isBlank() -> "Ingresa tus nombres"
            form.firstName.length > 50 -> "Maximo 50 caracteres"
            else -> null
        }

        val lastNameError = when {
            form.lastName.isBlank() -> "Ingresa tus apellidos"
            form.lastName.length > 100 -> "Maximo 100 caracteres"
            else -> null
        }

        val addressError = when {
            form.address.isBlank() -> "Ingresa tu direccion"
            form.address.length > 300 -> "Maximo 300 caracteres"
            else -> null
        }

        val regionError = if (form.region.isBlank()) {
            "Selecciona una region"
        } else null

        val communeError = if (form.commune.isBlank()) {
            "Selecciona una comuna"
        } else null

        val hasErrors = listOf(
            firstNameError,
            lastNameError,
            addressError,
            regionError,
            communeError
        ).any { it != null }

        if (hasErrors) {
            _uiState.update {
                it.copy(
                    personalErrors = PersonalErrors(
                        firstName = firstNameError,
                        lastName = lastNameError,
                        address = addressError,
                        region = regionError,
                        commune = communeError
                    )
                )
            }
            return
        }

        _uiState.update {
            it.copy(isSavingProfile = true, personalErrors = PersonalErrors())
        }

        viewModelScope.launch {
            delay(900)
            val updatedContent = content.copy(
                personalData = ProfilePersonalData(
                    firstName = form.firstName.trim(),
                    lastName = form.lastName.trim(),
                    email = form.email,
                    phone = form.phone.trim(),
                    birthDate = form.birthDate,
                    address = form.address.trim(),
                    region = form.region,
                    commune = form.commune
                ),
                preferences = ProfilePreferences(
                    shippingOptionId = preferences.shippingOptionId,
                    defaultCoupon = preferences.defaultCoupon.trim().uppercase(),
                    newsletter = preferences.newsletter,
                    saveAddress = preferences.saveAddress
                )
            )
            _uiState.update {
                it.copy(
                    isSavingProfile = false,
                    profileSuccess = true,
                    content = updatedContent,
                    personalForm = it.personalForm.copy(
                        firstName = updatedContent.personalData.firstName,
                        lastName = updatedContent.personalData.lastName,
                        phone = updatedContent.personalData.phone,
                        address = updatedContent.personalData.address,
                        region = updatedContent.personalData.region,
                        commune = updatedContent.personalData.commune
                    ),
                    preferenceForm = it.preferenceForm.copy(
                        defaultCoupon = updatedContent.preferences.defaultCoupon
                    )
                )
            }
        }
    }

    fun resetProfileSuccess() {
        _uiState.update {
            it.copy(profileSuccess = false)
        }
    }

    fun updatePassword() {
        val current = _uiState.value
        val content = current.content ?: return
        val form = current.passwordForm

        val currentError = if (form.current.isBlank() || form.current != content.storedPassword) {
            "Contrasena actual incorrecta"
        } else null

        val newError = if (form.new.length < 4 || form.new.length > 10) {
            "Debe tener entre 4 y 10 caracteres"
        } else null

        val confirmError = if (form.confirm != form.new) {
            "Debe coincidir con la nueva contrasena"
        } else null

        val hasErrors = listOf(currentError, newError, confirmError).any { it != null }
        if (hasErrors) {
            _uiState.update {
                it.copy(
                    passwordErrors = PasswordErrors(
                        current = currentError,
                        new = newError,
                        confirm = confirmError
                    )
                )
            }
            return
        }

        _uiState.update {
            it.copy(isUpdatingPassword = true, passwordErrors = PasswordErrors())
        }

        viewModelScope.launch {
            delay(900)
            val updatedContent = content.copy(storedPassword = form.new)
            _uiState.update {
                it.copy(
                    content = updatedContent,
                    isUpdatingPassword = false,
                    passwordSuccess = true,
                    passwordForm = PasswordForm()
                )
            }
        }
    }

    fun resetPasswordSuccess() {
        _uiState.update {
            it.copy(passwordSuccess = false)
        }
    }

    private fun findCommunes(region: String, content: ProfileContent?): List<String> {
        if (region.isBlank() || content == null) return emptyList()
        return content.regions.firstOrNull {
            it.name.equals(region, ignoreCase = true)
        }?.communes.orEmpty()
    }

    companion object {
        fun provideFactory(
            profileRepository: ProfileRepository,
            userDao: UserDao
        ) = viewModelFactory {
            initializer {
                ProfileViewModel(profileRepository, userDao)
            }
        }
    }
}
