package com.example.milsaboresapp.domain.model

/**
 * Copia y mensajes de la pagina de registro.
 */
data class RegisterContent(
    val headline: String,
    val subtitle: String,
    val submitLabel: String,
    val loginPrompt: String,
    val loginCta: String,
    val passwordHint: String,
    val benefits: List<AuthBenefit>,
    val legalDisclaimer: String
)
