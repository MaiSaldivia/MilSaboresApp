package com.example.milsaboresapp.data.session

import com.example.milsaboresapp.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Maneja el usuario actualmente logueado en memoria.
 * (No persiste entre reinicios de la app, pero sí mientras la app está abierta).
 */
class SessionManager {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun setCurrentUser(user: User?) {
        _currentUser.value = user
    }

    fun logout() {
        _currentUser.value = null
    }
}
