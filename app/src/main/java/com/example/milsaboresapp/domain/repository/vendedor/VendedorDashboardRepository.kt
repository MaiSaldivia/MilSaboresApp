package com.example.milsaboresapp.domain.repository.vendedor

import com.example.milsaboresapp.domain.model.vendedor.VendedorDashboard
import kotlinx.coroutines.flow.Flow

interface VendedorDashboardRepository {
    fun observeDashboard(): Flow<VendedorDashboard>
}
