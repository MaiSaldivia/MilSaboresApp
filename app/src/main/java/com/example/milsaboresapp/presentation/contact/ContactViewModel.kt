package com.example.milsaboresapp.presentation.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.ContactInfo
import com.example.milsaboresapp.domain.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactViewModel(
    private val contactRepository: ContactRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val contactInfo: ContactInfo? = null,
        val name: String = "",
        val email: String = "",
        val phone: String = "",
        val message: String = "",
        val eventType: String = "",
        val isSubmitting: Boolean = false,
        val submitSuccess: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            contactRepository.observeContactInfo().collect { info ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    contactInfo = info
                )
            }
        }
    }

    fun onNameChange(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun onPhoneChange(value: String) {
        _uiState.value = _uiState.value.copy(phone = value)
    }

    fun onMessageChange(value: String) {
        _uiState.value = _uiState.value.copy(message = value)
    }

    fun onEventTypeChange(value: String) {
        _uiState.value = _uiState.value.copy(eventType = value)
    }

    fun onSubmit() {
        val current = _uiState.value
        if (current.name.isBlank() || current.email.isBlank() || current.message.isBlank()) {
            return
        }
        _uiState.value = current.copy(isSubmitting = true)
        viewModelScope.launch {
            // Simular envio
            kotlinx.coroutines.delay(1200)
            _uiState.value = _uiState.value.copy(
                isSubmitting = false,
                submitSuccess = true,
                name = "",
                email = "",
                phone = "",
                message = "",
                eventType = ""
            )
        }
    }

    fun resetSuccess() {
        _uiState.value = _uiState.value.copy(submitSuccess = false)
    }

    companion object {
        fun provideFactory(
            contactRepository: ContactRepository
        ) = viewModelFactory {
            initializer {
                ContactViewModel(contactRepository)
            }
        }
    }
}
