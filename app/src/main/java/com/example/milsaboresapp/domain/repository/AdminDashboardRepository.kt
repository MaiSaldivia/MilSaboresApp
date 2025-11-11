package com.example.milsaboresapp.domain.repository

import com.example.milsaboresapp.domain.model.admin.AdminDashboard
import kotlinx.coroutines.flow.Flow

interface AdminDashboardRepository {
    fun observeDashboard(): Flow<AdminDashboard>
}
