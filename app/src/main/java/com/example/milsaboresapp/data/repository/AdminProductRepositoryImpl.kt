package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.AdminSeedData
import com.example.milsaboresapp.domain.model.admin.AdminProductFormContent
import com.example.milsaboresapp.domain.model.admin.AdminProductItem
import com.example.milsaboresapp.domain.repository.AdminProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class AdminProductRepositoryImpl : AdminProductRepository {

    private val productsState = MutableStateFlow(AdminSeedData.products)
    private val formState = MutableStateFlow(AdminSeedData.productFormContent)

    override fun observeProducts(): Flow<List<AdminProductItem>> = productsState.map { it }

    override fun observeProductForm(): Flow<AdminProductFormContent> = formState.map { it }

    override suspend fun addProduct(item: AdminProductItem) {
        productsState.value = listOf(item) + productsState.value.filterNot { it.code.equals(item.code, ignoreCase = true) }
    }

    override suspend fun deleteProduct(code: String) {
        productsState.value = productsState.value.filterNot { it.code.equals(code, ignoreCase = true) }
    }
}
