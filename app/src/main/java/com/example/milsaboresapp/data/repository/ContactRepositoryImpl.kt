package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.ContactSeedData
import com.example.milsaboresapp.domain.model.ContactInfo
import com.example.milsaboresapp.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class ContactRepositoryImpl : ContactRepository {

    private val contactState = MutableStateFlow(ContactSeedData.info)

    override fun observeContactInfo(): Flow<ContactInfo> =
        contactState.map { it }
}
