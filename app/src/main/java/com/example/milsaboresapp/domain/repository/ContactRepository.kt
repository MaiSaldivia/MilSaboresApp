package com.example.milsaboresapp.domain.repository

import com.example.milsaboresapp.domain.model.ContactInfo
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun observeContactInfo(): Flow<ContactInfo>
}
