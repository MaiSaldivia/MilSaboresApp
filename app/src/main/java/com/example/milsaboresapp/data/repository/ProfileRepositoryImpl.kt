package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.ProfileSeedData
import com.example.milsaboresapp.domain.model.ProfileContent
import com.example.milsaboresapp.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class ProfileRepositoryImpl : ProfileRepository {

    private val state = MutableStateFlow(ProfileSeedData.content)

    override fun observeProfileContent(): Flow<ProfileContent> = state.map { it }
}
