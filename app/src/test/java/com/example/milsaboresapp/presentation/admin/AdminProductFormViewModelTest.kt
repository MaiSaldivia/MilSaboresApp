package com.example.milsaboresapp.presentation.admin

import com.example.milsaboresapp.domain.model.admin.AdminProductFormContent
import com.example.milsaboresapp.domain.model.admin.AdminProductItem
import com.example.milsaboresapp.domain.repository.AdminProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdminProductFormViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldSignalValidationErrorsWhenSubmittingEmptyForm() = runTest(testDispatcher) {
        // Given
        val repository = FakeAdminProductRepository()
        val viewModel = AdminProductFormViewModel(repository)
        advanceUntilIdle()

        // When
        viewModel.submit()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertNotNull(state.errors.code)
        assertNotNull(state.errors.name)
        assertTrue(repository.addedItems.isEmpty())
        assertTrue(repository.updatedItems.isEmpty())
    }

    @Test
    fun shouldResetFormDefaultsWhenStartingCreate() = runTest(testDispatcher) {
        // Given
        val repository = FakeAdminProductRepository()
        val viewModel = AdminProductFormViewModel(repository)
        advanceUntilIdle()

        val item = AdminProductItem(
            code = "P1",
            name = "Producto",
            price = 5000,
            stock = 3,
            category = "Tortas"
        )

        // When
        viewModel.startEditing(item)
        viewModel.startCreate()

        // Then
        val state = viewModel.uiState.value
        assertEquals("", state.code)
        assertEquals(repository.formContent.categories.first(), state.category)
        assertFalse(state.isEditing)
    }

    @Test
    fun shouldUppercaseProductCodeOnInput() = runTest(testDispatcher) {
        val repository = FakeAdminProductRepository()
        val viewModel = AdminProductFormViewModel(repository)
        advanceUntilIdle()

        viewModel.onCodeChange("ab12")

        assertEquals("AB12", viewModel.uiState.value.code)
    }

    private class FakeAdminProductRepository : AdminProductRepository {
        val formContent = AdminProductFormContent(
            title = "Crear producto",
            subtitle = "Completa los campos",
            categories = listOf("Tortas", "Postres"),
            successMessage = "Producto guardado"
        )
        private val productsFlow = MutableStateFlow<List<AdminProductItem>>(emptyList())
        private val formFlow = MutableStateFlow(formContent)

        val addedItems = mutableListOf<AdminProductItem>()
        val updatedItems = mutableListOf<AdminProductItem>()
        val deletedItems = mutableListOf<String>()

        override fun observeProducts() = productsFlow

        override fun observeProductForm() = formFlow

        override suspend fun addProduct(item: AdminProductItem) {
            addedItems += item
        }

        override suspend fun updateProduct(item: AdminProductItem) {
            updatedItems += item
        }

        override suspend fun deleteProduct(code: String) {
            deletedItems += code
        }
    }
}
