// data/local/datasource/CartSeedData.kt
package com.example.milsaboresapp.data.local.datasource

import com.example.milsaboresapp.domain.model.CartBenefit
import com.example.milsaboresapp.domain.model.CartItem
import com.example.milsaboresapp.domain.model.ShippingOption

object CartSeedData {

    val headline = "Tu carrito listo para celebrar"
    val subtitle = "Revisa cantidades, mensajes y metodos de entrega antes de continuar."

    // El carrito ahora parte vacío; esto se usa solo si quieres probar con items de ejemplo.
    val initialItems = emptyList<CartItem>()

    val shippingOptions = listOf(
        ShippingOption(
            title = "Despacho a domicilio",
            description = "Cobertura en Gran Valparaiso con ventanas de entrega coordinadas segun tramo.",
            fee = 4500
        ),
        ShippingOption(
            title = "Retiro en taller",
            description = "Sin costo en Av. Independencia 456, Valparaiso. Coordina tu horario ideal.",
            fee = 0
        ),
        ShippingOption(
            title = "Entrega express",
            description = "Consultanos disponibilidad para pedidos con menos de 24 horas de antelacion.",
            fee = 7000
        )
    )

    val paymentMethods = listOf(
        "Transferencia bancaria",
        "Webpay",
        "Efectivo al retirar"
    )

    val benefits = listOf(
        CartBenefit(
            icon = "🎂",
            title = "Pasteleria artesanal",
            description = "Elaboramos cada pedido a mano el mismo dia de la entrega."
        ),
        CartBenefit(
            icon = "🥇",
            title = "Ingredientes seleccionados",
            description = "Usamos chocolates belgas, frutas frescas de temporada y harinas premium."
        ),
    )

    val summaryNotes = listOf(
        "Despachamos de martes a domingo en Gran Valparaiso.",
        "Puedes personalizar colores o sabores al confirmar el pedido.",
        "Indica si necesitas factura en el siguiente paso."
    )

    // 👇 ya NO usamos descuento base fijo
    const val discount = 0

    // 👇 lo dejamos en 0, el costo real viene desde ShippingOption.fee
    const val deliveryFee = 0

    const val assistanceMessage =
        "Si necesitas un formato especial escribenos a contacto@milsabores.cl o por WhatsApp."
    const val checkoutCta = "Continuar con el pago"
}
