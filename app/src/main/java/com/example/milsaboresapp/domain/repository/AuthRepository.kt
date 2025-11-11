package com.example.milsaboresapp.domain.repository

import com.example.milsaboresapp.domain.model.LoginContent
import com.example.milsaboresapp.domain.model.RegisterContent
import com.example.milsaboresapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun observeLoginContent(): Flow<LoginContent>
    fun observeRegisterContent(): Flow<RegisterContent>

    // 👉 NUEVO: flujo con el usuario logueado (o null)
    fun observeCurrentUser(): Flow<User?>

    /**
     * Registra un usuario.
     * @return null si todo OK, o mensaje de error si algo falla (correo ya usado, etc.)
     */
    suspend fun registerUser(
        run: String,
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        birthDate: String,
        region: String,
        commune: String,
        address: String,
        password: String,
        promoCode: String?,          // código FELICES50 o null
        acceptsPromotions: Boolean
    ): String?

    /**
     * Inicia sesión.
     * @return el usuario si las credenciales son correctas, o null en caso contrario.
     */
    suspend fun login(email: String, password: String): User?
}
