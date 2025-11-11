package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.AboutSeedData
import com.example.milsaboresapp.domain.model.AboutInfo
import com.example.milsaboresapp.domain.repository.AboutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class AboutRepositoryImpl : AboutRepository {

    private val aboutState = MutableStateFlow(AboutSeedData.info)

    override fun observeAboutInfo(): Flow<AboutInfo> =
        aboutState.map { it }
}
