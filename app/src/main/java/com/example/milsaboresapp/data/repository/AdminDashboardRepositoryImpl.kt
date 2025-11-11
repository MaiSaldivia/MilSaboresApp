package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.AdminSeedData
import com.example.milsaboresapp.domain.model.admin.AdminDashboard
import com.example.milsaboresapp.domain.repository.AdminDashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class AdminDashboardRepositoryImpl : AdminDashboardRepository {

    private val state = MutableStateFlow(AdminSeedData.dashboard)

    override fun observeDashboard(): Flow<AdminDashboard> = state.map { it }
}
