package com.example.milsaboresapp.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.admin.AdminProductFormContent
import com.example.milsaboresapp.domain.model.admin.AdminProductItem
import com.example.milsaboresapp.domain.repository.AdminProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminProductFormViewModel(
    private val productRepository: AdminProductRepository
) : ViewModel() {

    data class FormErrors(
        val code: String? = null,
        val name: String? = null,
        val price: String? = null,
        val stock: String? = null,
        val category: String? = null
    )

    data class UiState(
        val isLoading: Boolean = true,
        val content: AdminProductFormContent? = null,
        val code: String = "",
        val name: String = "",
        val price: String = "",
        val stock: String = "",
        val category: String = "",
        val errors: FormErrors = FormErrors(),
        val isSubmitting: Boolean = false,
        val submitSuccess: Boolean = false,
        val isEditing: Boolean = false,
        val editingCode: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            productRepository.observeProductForm().collect { content ->
                _uiState.update { current ->
                    val targetCategory = if (current.category.isNotBlank()) {
                        current.category
                    } else {
                        content.categories.firstOrNull().orEmpty()
                    }
                    current.copy(
                        isLoading = false,
                        content = content,
                        category = targetCategory
                    )
                }
            }
        }
    }

    fun onCodeChange(value: String) {
        _uiState.update {
            it.copy(code = value.uppercase(), errors = it.errors.copy(code = null))
        }
    }

    fun onNameChange(value: String) {
        _uiState.update {
            it.copy(name = value, errors = it.errors.copy(name = null))
        }
    }

    fun onPriceChange(value: String) {
        _uiState.update {
            it.copy(price = value.filter { char -> char.isDigit() }, errors = it.errors.copy(price = null))
        }
    }

    fun onStockChange(value: String) {
        _uiState.update {
            it.copy(stock = value.filter { char -> char.isDigit() }, errors = it.errors.copy(stock = null))
        }
    }

    fun onCategoryChange(value: String) {
        _uiState.update {
            it.copy(category = value, errors = it.errors.copy(category = null))
        }
    }

    fun startCreate() {
        _uiState.update { current ->
            val defaultCategory = current.content?.categories?.firstOrNull().orEmpty()
            current.copy(
                code = "",
                name = "",
                price = "",
                stock = "",
                category = defaultCategory,
                errors = FormErrors(),
                isSubmitting = false,
                submitSuccess = false,
                isEditing = false,
                editingCode = null
            )
        }
    }

    fun startEditing(item: AdminProductItem) {
        _uiState.update { current ->
            current.copy(
                isEditing = true,
                editingCode = item.code,
                code = item.code,
                name = item.name,
                price = item.price.toString(),
                stock = item.stock.toString(),
                category = item.category,
                errors = FormErrors(),
                submitSuccess = false,
                isSubmitting = false
            )
        }
    }

    fun submit() {
        val state = _uiState.value
        val content = state.content ?: return

        val codeError = when {
            state.code.isBlank() -> "Ingresa un codigo"
            state.code.length < 3 -> "Minimo 3 caracteres"
            else -> null
        }
        val nameError = if (state.name.isBlank()) "Ingresa un nombre" else null
        val priceValue = state.price.toIntOrNull()
        val priceError = if (priceValue == null || priceValue <= 0) "Precio invalido" else null
        val stockValue = state.stock.toIntOrNull()
        val stockError = if (stockValue == null || stockValue < 0) "Stock invalido" else null
        val categoryError = if (state.category.isBlank()) "Selecciona una categoria" else null

        val hasErrors = listOf(codeError, nameError, priceError, stockError, categoryError).any { it != null }
        if (hasErrors) {
            _uiState.update {
                it.copy(
                    errors = FormErrors(
                        code = codeError,
                        name = nameError,
                        price = priceError,
                        stock = stockError,
                        category = categoryError
                    )
                )
            }
            return
        }

        if (state.isSubmitting) return

        _uiState.update {
            it.copy(isSubmitting = true)
        }

        val targetCode = state.editingCode ?: state.code.trim()

        viewModelScope.launch {
            val item = AdminProductItem(
                code = targetCode,
                name = state.name.trim(),
                price = priceValue!!,
                stock = stockValue!!,
                category = state.category
            )
            if (state.isEditing) {
                productRepository.updateProduct(item)
            } else {
                productRepository.addProduct(item)
            }
            _uiState.update {
                it.copy(
                    code = if (state.isEditing) targetCode else "",
                    name = if (state.isEditing) state.name.trim() else "",
                    price = if (state.isEditing) priceValue.toString() else "",
                    stock = if (state.isEditing) stockValue.toString() else "",
                    isSubmitting = false,
                    submitSuccess = true,
                    errors = FormErrors(),
                    isEditing = state.isEditing,
                    editingCode = if (state.isEditing) targetCode else null
                )
            }
        }
    }

    fun resetSuccess() {
        _uiState.update {
            it.copy(submitSuccess = false)
        }
    }

    fun resetForm() {
        val content = _uiState.value.content
        _uiState.update {
            it.copy(
                code = "",
                name = "",
                price = "",
                stock = "",
                category = content?.categories?.firstOrNull().orEmpty(),
                errors = FormErrors(),
                isSubmitting = false,
                submitSuccess = false,
                isEditing = false,
                editingCode = null
            )
        }
    }

    companion object {
        fun provideFactory(
            productRepository: AdminProductRepository
        ) = viewModelFactory {
            initializer {
                AdminProductFormViewModel(productRepository)
            }
        }
    }
}
