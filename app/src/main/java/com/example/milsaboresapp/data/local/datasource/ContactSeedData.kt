package com.example.milsaboresapp.data.local.datasource

import com.example.milsaboresapp.domain.model.ContactChannel
import com.example.milsaboresapp.domain.model.ContactInfo

object ContactSeedData {
    val info = ContactInfo(
        headline = "Hablemos de tu proxima celebracion",
        description = "Nuestro equipo responde en menos de 24 horas para ayudarte a elegir la torta ideal o planificar un evento completo.",
        address = "Av. Independencia 456, Valparaiso",
        phone = "+56 9 8765 4321",
        email = "contacto@milsabores.cl",
        businessHours = "Lunes a Sabado, 09:00 a 19:00 hrs",
        serviceAreas = listOf(
            "Gran Valparaiso",
            "Vina del Mar",
            "Quilpue"
        ),
        channels = listOf(
            ContactChannel(label = "WhatsApp", value = "+56 9 8765 4321"),
            ContactChannel(label = "Instagram", value = "@milsabores.cl"),
            ContactChannel(label = "Facebook", value = "facebook.com/milsabores"),
            ContactChannel(label = "TikTok", value = "@milsabores.cl")
        )
    )
}
