package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.dao.ProductDao
import com.example.milsaboresapp.data.local.datasource.ProductLocalDataSource
import com.example.milsaboresapp.data.local.entity.ProductoEntity
import com.example.milsaboresapp.domain.model.admin.AdminProductItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdminProductRepositoryRoomImplTest {

    @Test
    fun observeProductsMapsRoomEntities() = runTest {
        val dao = RecordingProductDao()
        val dataSource = ProductLocalDataSource(dao, backgroundScope)
        val repository = AdminProductRepositoryRoomImpl(dataSource)

        advanceUntilIdle()

        val entity = ProductoEntity(
            id = "X1",
            nombre = "Cheesecake",
            precio = 1000,
            categoria = "Postres",
            attr = "",
            img = "",
            stock = 4,
            stockCritico = 1,
            descripcion = ""
        )
        dataSource.upsert(entity)
        advanceUntilIdle()

        val items = repository.observeProducts().first { list ->
            list.any { it.code == "X1" }
        }
        assertTrue(items.any { it.code == "X1" && it.name == "Cheesecake" })
    }

    @Test
    fun addProductDelegatesToLocalDataSource() = runTest {
        val dao = RecordingProductDao()
        val dataSource = ProductLocalDataSource(dao, backgroundScope)
        val repository = AdminProductRepositoryRoomImpl(dataSource)

        advanceUntilIdle()

        val item = AdminProductItem(
            code = "NEW",
            name = "Producto Nuevo",
            price = 2500,
            stock = 2,
            category = "Postres"
        )

        repository.addProduct(item)
    advanceUntilIdle()

        val stored = dao.getById("NEW")
        assertNotNull(stored)
        assertEquals("Producto Nuevo", stored!!.nombre)
        assertEquals(2500, stored.precio)
    }

    @Test
    fun deleteProductRemovesEntry() = runTest {
        val dao = RecordingProductDao()
        val dataSource = ProductLocalDataSource(dao, backgroundScope)
        val repository = AdminProductRepositoryRoomImpl(dataSource)

        advanceUntilIdle()

        val entity = ProductoEntity(
            id = "DEL",
            nombre = "Para borrar",
            precio = 1000,
            categoria = "Tortas",
            attr = "",
            img = "",
            stock = 1,
            stockCritico = 0,
            descripcion = ""
        )
        dataSource.upsert(entity)
        advanceUntilIdle()

        repository.deleteProduct("DEL")
        advanceUntilIdle()

        assertEquals(null, dao.getById("DEL"))
    }

    private class RecordingProductDao : ProductDao {
        private val state = MutableStateFlow<List<ProductoEntity>>(emptyList())

        override fun observeAll(): Flow<List<ProductoEntity>> = state.asStateFlow()

        override suspend fun getAll(): List<ProductoEntity> = state.value

        override suspend fun getById(id: String): ProductoEntity? =
            state.value.firstOrNull { it.id == id }

        override suspend fun insert(product: ProductoEntity) {
            insertAll(listOf(product))
        }

        override suspend fun insertAll(products: List<ProductoEntity>) {
            if (products.isEmpty()) return
            val map = state.value.associateBy { it.id }.toMutableMap()
            products.forEach { map[it.id] = it }
            state.value = map.values.sortedBy { it.id }
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
