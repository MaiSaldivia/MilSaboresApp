package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.dao.UserDao
import com.example.milsaboresapp.data.local.datasource.AuthSeedData
import com.example.milsaboresapp.data.local.entity.UserEntity
import com.example.milsaboresapp.data.session.SessionManager
import com.example.milsaboresapp.domain.model.LoginContent
import com.example.milsaboresapp.domain.model.RegisterContent
import com.example.milsaboresapp.domain.model.User
import com.example.milsaboresapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : AuthRepository {

    override fun observeLoginContent(): Flow<LoginContent> = flow {
        emit(AuthSeedData.loginContent)
    }

    override fun observeRegisterContent(): Flow<RegisterContent> = flow {
        emit(AuthSeedData.registerContent)
    }

    // 👉 implementación que faltaba
    override fun observeCurrentUser(): Flow<User?> = sessionManager.currentUser

    // Ahora también dejamos la sesión iniciada al registrar
    override suspend fun registerUser(
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
        promoCode: String?,
        acceptsPromotions: Boolean
    ): String? = withContext(Dispatchers.IO) {

        val cleanEmail = email.trim()

        val existing = userDao.getByEmail(cleanEmail)
        if (existing != null) {
            return@withContext "Ya existe una cuenta registrada con ese correo."
        }

        val entity = UserEntity(
            run = run.trim(),
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            email = cleanEmail,
            phone = phone.trim(),
            birthDate = birthDate.trim(),
            region = region.trim(),
            commune = commune.trim(),
            address = address.trim(),
            password = password,
            promoCode = promoCode?.uppercase()   // asegúrate que UserEntity tenga este campo
        )

        userDao.insert(entity)

        // Volvemos a leer el usuario recién insertado para obtener su id
        val inserted = userDao.getByEmail(cleanEmail)
        if (inserted != null) {
            val user = User(
                id = inserted.id,
                name = "${inserted.firstName} ${inserted.lastName}".trim(),
                email = inserted.email
            )
            // Deja la sesión iniciada automáticamente
            sessionManager.setCurrentUser(user)
        }

        null
    }

    override suspend fun login(email: String, password: String): User? =
        withContext(Dispatchers.IO) {
            val entity = userDao.login(email.trim(), password)
            entity?.let {
                val user = User(
                    id = it.id,
                    name = "${it.firstName} ${it.lastName}".trim(),
                    email = it.email
                )
                // Guardamos usuario logueado en el SessionManager
                sessionManager.setCurrentUser(user)
                user
            }
        }
}
