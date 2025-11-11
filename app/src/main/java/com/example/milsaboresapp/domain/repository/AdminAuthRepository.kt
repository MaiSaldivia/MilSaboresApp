package com.example.milsaboresapp.domain.repository

import com.example.milsaboresapp.domain.model.admin.AdminLoginContent
import kotlinx.coroutines.flow.Flow

interface AdminAuthRepository {
    fun observeLoginContent(): Flow<AdminLoginContent>
}
