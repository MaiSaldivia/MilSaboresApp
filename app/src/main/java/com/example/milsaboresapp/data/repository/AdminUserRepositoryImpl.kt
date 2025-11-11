package com.example.milsaboresapp.data.repository

import com.example.milsaboresapp.data.local.datasource.AdminSeedData
import com.example.milsaboresapp.domain.model.admin.AdminUserFormContent
import com.example.milsaboresapp.domain.model.admin.AdminUserItem
import com.example.milsaboresapp.domain.repository.AdminUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class AdminUserRepositoryImpl : AdminUserRepository {

    private val usersState = MutableStateFlow(AdminSeedData.users)
    private val formState = MutableStateFlow(AdminSeedData.userFormContent)

    override fun observeUsers(): Flow<List<AdminUserItem>> = usersState.map { it }

    override fun observeUserForm(): Flow<AdminUserFormContent> = formState.map { it }

    override suspend fun addUser(item: AdminUserItem) {
        usersState.value = listOf(item) + usersState.value.filterNot { it.run.equals(item.run, ignoreCase = true) }
    }

    override suspend fun deleteUser(run: String) {
        usersState.value = usersState.value.filterNot { it.run.equals(run, ignoreCase = true) }
    }
}
