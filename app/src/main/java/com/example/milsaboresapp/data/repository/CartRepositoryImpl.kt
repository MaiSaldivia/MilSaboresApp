// data/repository/CartRepositoryImpl.kt
package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.CartSeedData
import com.example.milsaboresapp.domain.model.CartInfo
import com.example.milsaboresapp.domain.model.CartItem
import com.example.milsaboresapp.domain.model.CartSummary
import com.example.milsaboresapp.domain.model.Producto
import com.example.milsaboresapp.domain.model.ShippingOption
import com.example.milsaboresapp.domain.model.User
import com.example.milsaboresapp.domain.repository.CartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class CartRepositoryImpl(
    userFlow: Flow<User?>
) : CartRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val cartState = MutableStateFlow(buildCart(emptyList()))

    @Volatile
    private var currentUser: User? = null

    @Volatile
    private var selectedShippingOption: ShippingOption? =
        CartSeedData.shippingOptions.firstOrNull()

    init {
        scope.launch {
            userFlow.collectLatest { user ->
                currentUser = user
                cartState.update { current ->
                    buildCart(current.items)
                }
            }
        }
    }

    override fun observeCart(): Flow<CartInfo> = cartState

    override suspend fun addItem(
        product: Producto,
        quantity: Int,
        message: String?
    ) {
        if (quantity <= 0) return

        cartState.update { current ->
            val existingIndex = current.items.indexOfFirst {
                it.productId == product.id && it.message == message
            }

            val updatedItems = if (existingIndex >= 0) {
                current.items.mapIndexed { index, item ->
                    if (index == existingIndex) item.copy(quantity = item.quantity + quantity)
                    else item
                }
            } else {
                val newItem = CartItem(
                    id = generateItemId(current.items),
                    productId = product.id,
                    name = product.nombre,
                    variant = product.attr,
                    message = message,
                    unitPrice = product.precio,
                    quantity = quantity,
                    image = product.img
                )
                current.items + newItem
            }

            buildCart(updatedItems)
        }
    }

    override suspend fun setQuantity(itemId: String, quantity: Int) {
        if (quantity < 1) {
            removeItem(itemId)
            return
        }
        cartState.update { current ->
            val updatedItems = current.items.map { item ->
                if (item.id == itemId) item.copy(quantity = quantity) else item
            }
            buildCart(updatedItems)
        }
    }

    override suspend fun removeItem(itemId: String) {
        cartState.update { current ->
            val updatedItems = current.items.filterNot { it.id == itemId }
            buildCart(updatedItems)
        }
    }

    override suspend fun clearCart() {
        cartState.value = buildCart(emptyList())
    }

    override suspend fun setShippingOption(option: ShippingOption) {
        selectedShippingOption = option
        cartState.update { current ->
            buildCart(current.items)
        }
    }

    // --------- Helpers internos ---------

    private fun generateItemId(existing: List<CartItem>): String {
        val maxExisting = existing
            .mapNotNull { it.id.toIntOrNull() }
            .maxOrNull() ?: 0
        return (maxExisting + 1).toString()
    }

    private fun buildCart(items: List<CartItem>): CartInfo {
        val normalizedItems = items.map { it.copy() }
        val subtotal = normalizedItems.sumOf { it.unitPrice * it.quantity }

        var discount = 0
        val extraNotes = mutableListOf<String>()

        val user = currentUser

        if (user != null && normalizedItems.isNotEmpty()) {
            val age = calculateAge(user.birthDate)
            if (age != null && age >= 50) {
                val ageDiscount = subtotal * 50 / 100
                discount += ageDiscount
                extraNotes += "50% de descuento por ser mayor de 50 años."
            }

            if (user.promoCode?.uppercase() == "FELICES50") {
                val promoDiscount = subtotal * 10 / 100
                discount += promoDiscount
                extraNotes += "10% de descuento por código FELICES50."
            }

            if (isDuocEmail(user.email) && isBirthdayToday(user.birthDate)) {
                val freeCakeItem = normalizedItems
                    .filter { it.name.contains("Torta", ignoreCase = true) }
                    .minByOrNull { it.unitPrice }

                freeCakeItem?.let { cake ->
                    discount += cake.unitPrice
                    extraNotes += "Torta gratis por cumpleaños DUOC."
                }
            }
        }

        val deliveryFee =
            if (normalizedItems.isEmpty()) 0 else selectedShippingOption?.fee ?: 0

        val total = (subtotal - discount).coerceAtLeast(0) + deliveryFee

        val summaryNotes = if (normalizedItems.isEmpty()) {
            listOf(
                "Tu carrito esta vacio por ahora.",
                "Explora la seccion de productos para agregar tus sabores favoritos."
            )
        } else {
            CartSeedData.summaryNotes +
                    (selectedShippingOption?.let { "Modo de entrega: ${it.title}." }?.let { listOf(it) }
                        ?: emptyList()) +
                    extraNotes
        }

        return CartInfo(
            headline = CartSeedData.headline,
            subtitle = CartSeedData.subtitle,
            items = normalizedItems,
            summary = CartSummary(
                subtotal = subtotal,
                discount = discount,
                deliveryFee = deliveryFee,
                total = total,
                notes = summaryNotes
            ),
            shippingOptions = CartSeedData.shippingOptions,
            paymentMethods = CartSeedData.paymentMethods,
            benefits = CartSeedData.benefits,
            assistanceMessage = CartSeedData.assistanceMessage,
            checkoutCta = CartSeedData.checkoutCta
        )
    }

    // ----------------- DESCUENTOS: HELPERS -----------------

    private fun calculateAge(birthDate: String): Int? { /* igual que ya tenías */
        if (!birthDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) return null
        val parts = birthDate.split("-")
        val year = parts[0].toIntOrNull() ?: return null
        val month = parts[1].toIntOrNull() ?: return null
        val day = parts[2].toIntOrNull() ?: return null

        return try {
            val cal = Calendar.getInstance()
            var age = cal.get(Calendar.YEAR) - year
            val currentMonth = cal.get(Calendar.MONTH) + 1
            val currentDay = cal.get(Calendar.DAY_OF_MONTH)

            if (month > currentMonth || (month == currentMonth && day > currentDay)) {
                age -= 1
            }
            age
        } catch (_: Exception) {
            null
        }
    }

    private fun isBirthdayToday(birthDate: String): Boolean { /* igual que antes */
        if (!birthDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) return false
        val parts = birthDate.split("-")
        val month = parts[1].toIntOrNull() ?: return false
        val day = parts[2].toIntOrNull() ?: return false

        val cal = Calendar.getInstance()
        val currentMonth = cal.get(Calendar.MONTH) + 1
        val currentDay = cal.get(Calendar.DAY_OF_MONTH)

        return month == currentMonth && day == currentDay
    }

    private fun isDuocEmail(email: String): Boolean {
        val lower = email.lowercase()
        return lower.endsWith("@duoc.cl") || lower.endsWith("@profesor.duoc.cl")
    }
}
