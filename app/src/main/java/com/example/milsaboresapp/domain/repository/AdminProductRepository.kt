package com.example.milsaboresapp.domain.repository

import com.example.milsaboresapp.domain.model.admin.AdminProductFormContent
import com.example.milsaboresapp.domain.model.admin.AdminProductItem
import kotlinx.coroutines.flow.Flow

interface AdminProductRepository {
    fun observeProducts(): Flow<List<AdminProductItem>>
    fun observeProductForm(): Flow<AdminProductFormContent>
    suspend fun addProduct(item: AdminProductItem)
    suspend fun deleteProduct(code: String)
}
