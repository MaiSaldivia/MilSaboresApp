package com.example.milsaboresapp.presentation.auth

private val allowedDomains = listOf(
    "duoc.cl",
    "profesor.duoc.cl",
    "gmail.com"
)

/**
 * Valida el correo según las reglas del enunciado.
 * Devuelve null si es válido, o un mensaje de error en caso contrario.
 */
fun validateEmailForAuth(raw: String): String? {
    val email = raw.trim()

    if (email.isEmpty()) {
        return "El correo es obligatorio."
    }
    if (email.length > 100) {
        return "El correo no puede superar los 100 caracteres."
    }

    val atIndex = email.lastIndexOf('@')
    if (atIndex <= 0 || atIndex == email.lastIndex) {
        return "Debes ingresar un correo válido."
    }

    val domain = email.substring(atIndex + 1)
    if (allowedDomains.none { domain.equals(it, ignoreCase = true) }) {
        return "Solo se permiten correos @duoc.cl, @profesor.duoc.cl o @gmail.com."
    }

    return null
}

/**
 * Valida contraseña para login/registro: obligatoria y entre 4 y 10 caracteres.
 */
fun validatePasswordForAuth(password: String): String? {
    if (password.isEmpty()) {
        return "La contraseña es obligatoria."
    }
    if (password.length !in 4..10) {
        return "La contraseña debe tener entre 4 y 10 caracteres."
    }
    return null
}
