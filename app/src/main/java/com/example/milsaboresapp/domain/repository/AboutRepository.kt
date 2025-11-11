package com.example.milsaboresapp.domain.repository

import com.example.milsaboresapp.domain.model.AboutInfo
import kotlinx.coroutines.flow.Flow

interface AboutRepository {
    fun observeAboutInfo(): Flow<AboutInfo>
}
