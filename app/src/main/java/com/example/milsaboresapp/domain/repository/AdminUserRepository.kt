package com.example.milsaboresapp.domain.repository

import com.example.milsaboresapp.domain.model.admin.AdminUserFormContent
import com.example.milsaboresapp.domain.model.admin.AdminUserItem
import kotlinx.coroutines.flow.Flow

interface AdminUserRepository {
    fun observeUsers(): Flow<List<AdminUserItem>>
    fun observeUserForm(): Flow<AdminUserFormContent>
    suspend fun addUser(item: AdminUserItem)
    suspend fun deleteUser(run: String)
}
