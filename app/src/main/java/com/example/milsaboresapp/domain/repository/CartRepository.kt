// domain/repository/CartRepository.kt
package com.example.milsaboresapp.domain.repository

import com.example.milsaboresapp.domain.model.CartInfo
import com.example.milsaboresapp.domain.model.Producto
import com.example.milsaboresapp.domain.model.ShippingOption
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun observeCart(): Flow<CartInfo>

    suspend fun addItem(
        product: Producto,
        quantity: Int,
        message: String? = null
    )

    suspend fun setQuantity(itemId: String, quantity: Int)
    suspend fun removeItem(itemId: String)
    suspend fun clearCart()

    // 🆕 seleccionar tipo de entrega
    suspend fun setShippingOption(option: ShippingOption)
}
