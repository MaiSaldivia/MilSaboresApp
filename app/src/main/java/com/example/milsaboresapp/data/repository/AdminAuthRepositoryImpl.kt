package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.AdminSeedData
import com.example.milsaboresapp.domain.model.admin.AdminLoginContent
import com.example.milsaboresapp.domain.repository.AdminAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class AdminAuthRepositoryImpl : AdminAuthRepository {

    private val state = MutableStateFlow(AdminSeedData.loginContent)

    override fun observeLoginContent(): Flow<AdminLoginContent> = state.map { it }
}
