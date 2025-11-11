package com.example.milsaboresapp.domain.model

/**
 * Copia y mensajes de la pagina de inicio de sesion.
 */
data class LoginContent(
    val headline: String,
    val subtitle: String,
    val rememberLabel: String,
    val forgotPasswordLabel: String,
    val submitLabel: String,
    val registerPrompt: String,
    val registerCta: String,
    val supportMessage: String,
    val benefits: List<AuthBenefit>
)
