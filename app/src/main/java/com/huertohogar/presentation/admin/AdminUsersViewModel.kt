package com.huertohogar.presentation.admin

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.db.AppDatabase
import com.huertohogar.data.model.User
import com.huertohogar.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminUsersUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class AdminUsersViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUsersUiState(isLoading = true))
    val uiState: StateFlow<AdminUsersUiState> = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                userRepository.getAllUsers().collect { users ->
                    _uiState.value = _uiState.value.copy(
                        users = users,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            try {
                userRepository.registerUser(user)
                // Recargar lista
                loadUsers()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                userRepository.updateUser(user)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun deleteUser(email: String) {
        viewModelScope.launch {
            try {
                userRepository.deleteUser(email)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AdminUsersViewModel::class.java)) {
                val database = AppDatabase.getDatabase(application)
                val userDao = database.userDao()
                val repository = UserRepository(userDao)
                @Suppress("UNCHECKED_CAST")
                return AdminUsersViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
