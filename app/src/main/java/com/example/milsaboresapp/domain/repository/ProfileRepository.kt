package com.example.milsaboresapp.domain.repository

import com.example.milsaboresapp.domain.model.ProfileContent
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfileContent(): Flow<ProfileContent>
}
