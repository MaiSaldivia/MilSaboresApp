package com.example.milsaboresapp.data.local.datasource

import com.example.milsaboresapp.domain.model.AuthBenefit
import com.example.milsaboresapp.domain.model.LoginContent
import com.example.milsaboresapp.domain.model.RegisterContent

object AuthSeedData {

    val loginContent = LoginContent(
        headline = "Bienvenido de vuelta",
        subtitle = "Revisa tus pedidos, personaliza tortas y recibe seguimiento personalizado.",
        rememberLabel = "Recordarme",
        forgotPasswordLabel = "Olvide mi contrasena",
        submitLabel = "Ingresar",
        registerPrompt = "Aun no tienes cuenta",
        registerCta = "Crear una cuenta",
        supportMessage = "Si necesitas ayuda escribe a soporte@milsabores.cl",
        benefits = listOf(
            AuthBenefit(
                title = "Historial dulce",
                description = "Encuentra tus pedidos anteriores para repetir sabores o ajustar cantidades."
            ),
            AuthBenefit(
                title = "Seguimiento en tiempo real",
                description = "Recibe notificaciones cuando tu pedido cambie de estado."
            ),
            AuthBenefit(
                title = "Recomendaciones personalizadas",
                description = "Te sugerimos sabores segun la estacion y tus preferencias."
            )
        )
    )

    val registerContent = RegisterContent(
        headline = "Crea tu cuenta Mil Sabores",
        subtitle = "Guarda tus pedidos favoritos, cotiza mas rapido y recibe adelantos de nuevas colecciones.",
        submitLabel = "Registrarme",
        loginPrompt = "Ya tienes una cuenta",
        loginCta = "Inicia sesion",

        // 👇 NUEVO TEXTO CON LOS REQUISITOS DEL ENUNCIADO
        passwordHint = "Tu contrasena debe tener entre 4 y 10 caracteres. " +
                "Solo se permiten correos @duoc.cl, @profesor.duoc.cl o @gmail.com.",

        benefits = listOf(
            AuthBenefit(
                title = "Cotizaciones express",
                description = "Completa tus datos una vez y reutilizalos en cada celebracion."
            ),
            AuthBenefit(
                title = "Club de lanzamientos",
                description = "Probaras primero nuestras recetas de temporada y ediciones limitadas."
            ),
            AuthBenefit(
                title = "Descuentos sorpresa",
                description = "Recibe cupones en fechas claves como aniversarios y cumpleanos."
            )
        ),
        legalDisclaimer = "Al registrarte aceptas nuestros terminos de servicio y politicas de privacidad."
    )
}
