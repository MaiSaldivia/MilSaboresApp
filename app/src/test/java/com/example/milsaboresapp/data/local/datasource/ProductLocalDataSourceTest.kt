package com.example.milsaboresapp.data.local.datasource

import com.example.milsaboresapp.data.local.dao.ProductDao
import com.example.milsaboresapp.data.local.entity.ProductoEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductLocalDataSourceTest {

    @Test
    fun seedPopulatesDaoWhenEmpty() = runTest {
        val dao = RecordingProductDao()
        val dataSource = ProductLocalDataSource(dao, backgroundScope)

        advanceUntilIdle()

        val products = dataSource.observeProducts().first { it.isNotEmpty() }
        assertTrue(products.isNotEmpty())
        assertTrue(dao.insertAllCalls >= 1)
    }

    @Test
    fun upsertKeepsSeedMetadata() = runTest {
        val seed = ProductSeedData.productos.first { it.id == "TC001" }
        val dao = RecordingProductDao(initial = listOf(seed))
        val dataSource = ProductLocalDataSource(dao, backgroundScope)

        advanceUntilIdle()

        val incoming = seed.copy(attr = "", img = "", descripcion = "")
        dataSource.upsert(incoming)

        val stored = dao.getAll().first { it.id == "TC001" }
        assertEquals(seed.attr, stored.attr)
        assertEquals(seed.img, stored.img)
        assertEquals(seed.descripcion, stored.descripcion)
    }

    @Test
    fun deleteRemovesProductFromCatalog() = runTest {
        val product = ProductoEntity(
            id = "DEL",
            nombre = "Para Borrar",
            precio = 1000,
            categoria = "Tortas",
            attr = "",
            img = "",
            stock = 1,
            stockCritico = 0,
            descripcion = ""
        )
        val dao = RecordingProductDao(initial = listOf(product))
        val dataSource = ProductLocalDataSource(dao, backgroundScope)

        advanceUntilIdle()

        dataSource.delete("DEL")

        val remaining = dao.getAll()
        assertFalse(remaining.any { it.id == "DEL" })
    }

    private class RecordingProductDao(initial: List<ProductoEntity> = emptyList()) : ProductDao {
        private val state = MutableStateFlow(initial.sortedBy { it.nombre })
        var insertAllCalls: Int = 0

        override fun observeAll(): Flow<List<ProductoEntity>> = state.asStateFlow()

        override suspend fun getAll(): List<ProductoEntity> = state.value

        override suspend fun getById(id: String): ProductoEntity? =
            state.value.firstOrNull { it.id == id }

        override suspend fun insert(product: ProductoEntity) {
            insertAll(listOf(product))
        }

        override suspend fun insertAll(products: List<ProductoEntity>) {
            if (products.isEmpty()) return
            insertAllCalls += 1
            val map = state.value.associateBy { it.id }.toMutableMap()
            products.forEach { map[it.id] = it }
            state.value = map.values.sortedBy { it.nombre }
        }

        override suspend fun update(product: ProductoEntity) {
            insert(product)
        }

        override suspend fun deleteById(id: String) {
            state.value = state.value.filterNot { it.id == id }
        }

        override suspend fun findByName(name: String): ProductoEntity? =
            state.value.firstOrNull { it.nombre.equals(name, ignoreCase = true) }
    }
}
