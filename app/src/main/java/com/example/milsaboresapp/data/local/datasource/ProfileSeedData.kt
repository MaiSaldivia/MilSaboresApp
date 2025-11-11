package com.example.milsaboresapp.data.local.datasource

import com.example.milsaboresapp.domain.model.ProfileContent
import com.example.milsaboresapp.domain.model.ProfilePersonalData
import com.example.milsaboresapp.domain.model.ProfilePreferences
import com.example.milsaboresapp.domain.model.ProfileRegion
import com.example.milsaboresapp.domain.model.ProfileShippingOption

object ProfileSeedData {

    val content = ProfileContent(
        headline = "Mi Perfil",
        welcomeMessage = "Estas editando el perfil de camila@milsabores.cl",
        personalSectionTitle = "Datos personales",
        preferencesSubtitle = "Preferencias de compra",
        saveChangesLabel = "Guardar cambios",
        defaultShippingHelper = "Se usara en el carrito si no has elegido otro.",
        defaultCouponHelper = "Se aplicara automaticamente si esta vacio el cupon del carrito.",
        newsletterLabel = "Quiero recibir promociones por email",
        saveAddressLabel = "Guardar direccion para proximos pedidos",
        passwordSectionTitle = "Cambiar contrasena",
        passwordSubmitLabel = "Actualizar contrasena",
        passwordHelper = "Tu contrasena debe tener de 4 a 10 caracteres.",
        currentPasswordLabel = "Contrasena actual",
        newPasswordLabel = "Nueva contrasena",
        confirmPasswordLabel = "Repetir nueva contrasena",
        personalData = ProfilePersonalData(
            firstName = "Camila",
            lastName = "Soto Perez",
            email = "camila@milsabores.cl",
            phone = "+56 9 8765 4321",
            birthDate = "1994-04-18",
            address = "Pasaje Los Dulces 123, Valparaiso",
            region = "Valparaiso",
            commune = "Valparaiso"
        ),
        preferences = ProfilePreferences(
            shippingOptionId = "pickup",
            defaultCoupon = "ENVIOGRATIS",
            newsletter = true,
            saveAddress = true
        ),
        regions = listOf(
            ProfileRegion(
                name = "Valparaiso",
                communes = listOf("Valparaiso", "Vina del Mar", "Quilpue", "Concon")
            ),
            ProfileRegion(
                name = "Region Metropolitana",
                communes = listOf("Santiago", "Providencia", "Las Condes", "Nunoa")
            ),
            ProfileRegion(
                name = "Biobio",
                communes = listOf("Concepcion", "Talcahuano", "San Pedro de la Paz")
            )
        ),
        shippingOptions = listOf(
            ProfileShippingOption(
                id = "pickup",
                label = "Retiro en tienda (gratis)",
                description = "Disponible en Valparaiso de lunes a sabado."
            ),
            ProfileShippingOption(
                id = "urban",
                label = "Envio urbano ($3.000)",
                description = "Cobertura en Gran Valparaiso en 24 horas."
            ),
            ProfileShippingOption(
                id = "regional",
                label = "Envio regional ($6.000)",
                description = "Despacho a regiones en 48-72 horas."
            )
        ),
        storedPassword = "1234"
    )
}
