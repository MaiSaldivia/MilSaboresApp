package com.example.milsaboresapp.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsaboresapp.domain.model.admin.AdminUserItem
import com.example.milsaboresapp.domain.repository.AdminUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminUsersViewModel(
    private val userRepository: AdminUserRepository
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val query: String = "",
        val selectedRole: String? = null,
        val roles: List<String> = emptyList(),
        val users: List<AdminUserItem> = emptyList(),
        val deleteSuccess: Boolean = false
    ) {
        val hasUsers: Boolean get() = users.isNotEmpty()
    }

    private val query = MutableStateFlow("")
    private val selectedRole = MutableStateFlow<String?>(null)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                userRepository.observeUsers(),
                userRepository.observeUserForm(),
                query,
                selectedRole
            ) { users, form, q, role ->
                val filtered = users.filter { user ->
                    val matchesQuery = q.isBlank() || user.run.contains(q, ignoreCase = true) ||
                        user.firstName.contains(q, ignoreCase = true) ||
                        user.lastName.contains(q, ignoreCase = true) ||
                        user.email.contains(q, ignoreCase = true)
                    val matchesRole = role.isNullOrBlank() || user.role.equals(role, ignoreCase = true)
                    matchesQuery && matchesRole
                }
                Triple(filtered, form.roles, Pair(q, role))
            }.collect { (filtered, roles, values) ->
                val (currentQuery, currentRole) = values
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        query = currentQuery,
                        selectedRole = currentRole,
                        roles = roles,
                        users = filtered
                    )
                }
            }
        }
    }

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun onRoleChange(value: String?) {
        selectedRole.value = value
    }

    fun deleteUser(run: String) {
        viewModelScope.launch {
            userRepository.deleteUser(run)
            _uiState.update {
                it.copy(deleteSuccess = true)
            }
        }
    }

    fun resetDeleteSuccess() {
        _uiState.update {
            it.copy(deleteSuccess = false)
        }
    }

    companion object {
        fun provideFactory(
            userRepository: AdminUserRepository
        ) = viewModelFactory {
            initializer {
                AdminUsersViewModel(userRepository)
            }
        }
    }
}
