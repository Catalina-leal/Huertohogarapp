package com.huertohogar.presentation.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.db.AppDatabase
import com.huertohogar.data.model.User
import com.huertohogar.data.repository.UserPreferencesRepository
import com.huertohogar.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first // <-- ¡Importación añadida aquí!
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            val email = preferencesRepository.userEmail.first() // <-- ¡Corrección aplicada aquí!
            if (email != null) {
                userRepository.getUser(email).collect { user ->
                    _currentUser.value = user
                }
            }
        }
    }

    fun updateUser(
        fullName: String,
        phone: String,
        address: String,
        city: String,
        region: String,
        postalCode: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val currentUser = _currentUser.value
                if (currentUser != null) {
                    val updatedUser = currentUser.copy(
                        phone = phone,
                        address = address,
                        city = city,
                        region = region,
                        postalCode = postalCode
                    )
                    userRepository.updateUser(updatedUser)
                    _currentUser.value = updatedUser
                }
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al actualizar: ${e.message}"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferencesRepository.clearLoginState()
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                val database = AppDatabase.getDatabase(application)
                val userDao = database.userDao()
                val userRepository = UserRepository(userDao)
                val preferencesRepository = UserPreferencesRepository(application.applicationContext)

                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(userRepository, preferencesRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
