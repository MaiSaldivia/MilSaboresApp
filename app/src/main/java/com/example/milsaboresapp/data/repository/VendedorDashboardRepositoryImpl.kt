package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.VendedorSeedData
import com.example.milsaboresapp.domain.model.vendedor.VendedorDashboard
import com.example.milsaboresapp.domain.repository.vendedor.VendedorDashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class VendedorDashboardRepositoryImpl : VendedorDashboardRepository {

    private val dashboardState = MutableStateFlow(VendedorSeedData.dashboard)

    override fun observeDashboard(): Flow<VendedorDashboard> = dashboardState.map { it }
}
