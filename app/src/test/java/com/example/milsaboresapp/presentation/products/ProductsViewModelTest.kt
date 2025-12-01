package com.example.milsaboresapp.presentation.products

import com.example.milsaboresapp.domain.model.Producto
import com.example.milsaboresapp.domain.repository.ProductoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModelTest {

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
    fun shouldEmitProductsSortedAscendingOnInit() = runTest {
        // Given
        val repository = FakeProductoRepository()
        repository.setProducts(
            listOf(
                producto(id = "2", nombre = "Brownie", categoria = "Postres Individuales"),
                producto(id = "1", nombre = "Alfajor", categoria = "Pastelería Tradicional")
            )
        )
        repository.setCategories(listOf("Tortas", "Postres"))

        // When
        val viewModel = ProductsViewModel(repository)
        advanceUntilIdle()

        // Then
        val names = viewModel.uiState.value.products.map { it.nombre }
        assertEquals(listOf("Alfajor", "Brownie"), names)
    }

    @Test
    fun shouldFilterProductsByQueryAcrossFields() = runTest {
        // Given
        val repository = FakeProductoRepository()
        repository.setProducts(
            listOf(
                producto(id = "1", nombre = "Torta Vegana", categoria = "Productos Vegana", attr = "Vegano"),
                producto(id = "2", nombre = "Mousse de Chocolate", categoria = "Postres Individuales"),
                producto(id = "3", nombre = "Brownie", categoria = "Productos Sin Gluten")
            )
        )
        repository.setCategories(listOf("Tortas", "Postres"))

        val viewModel = ProductsViewModel(repository)
        advanceUntilIdle()

        // When
        viewModel.onQueryChange("vegana")
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals(1, state.products.size)
        assertEquals("Torta Vegana", state.products.first().nombre)
    }

    @Test
    fun shouldGroupAllTortaCategoriesTogether() = runTest {
        // Given
        val repository = FakeProductoRepository()
        repository.setProducts(
            listOf(
                producto(id = "1", nombre = "Torta Cuadrada", categoria = "Tortas Cuadradas"),
                producto(id = "2", nombre = "Torta Circular", categoria = "Tortas Circulares"),
                producto(id = "3", nombre = "Mousse", categoria = "Postres Individuales")
            )
        )
        repository.setCategories(listOf("Tortas", "Postres"))

        val viewModel = ProductsViewModel(repository)
        advanceUntilIdle()

        // When
        viewModel.onCategorySelected("Tortas")
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals(2, state.products.size)
        assertTrue(state.products.all { it.categoria.contains("Torta", ignoreCase = true) })
    }

    private fun producto(
        id: String,
        nombre: String,
        categoria: String,
        precio: Int = 1000,
        attr: String = "",
        img: String = "",
        stock: Int = 1,
        stockCritico: Int = 0,
        descripcion: String = ""
    ) = Producto(
        id = id,
        nombre = nombre,
        precio = precio,
        categoria = categoria,
        attr = attr,
        img = img,
        stock = stock,
        stockCritico = stockCritico,
        descripcion = descripcion
    )

    private class FakeProductoRepository : ProductoRepository {
        private val products = MutableStateFlow<List<Producto>>(emptyList())
        private val categories = MutableStateFlow<List<String>>(emptyList())

        fun setProducts(value: List<Producto>) {
            products.value = value
        }

        fun setCategories(value: List<String>) {
            categories.value = value
        }

        override fun observeProductos() = products

        override fun observeDestacados(limit: Int) = products.map { list ->
            list.sortedBy { it.precio }.take(limit)
        }

        override fun observeCategorias() = categories

        override suspend fun findById(id: String): Producto? =
            products.value.firstOrNull { it.id == id }
    }
}
