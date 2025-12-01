package com.huertohogar.presentation.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.db.AppDatabase
import com.huertohogar.data.model.User
import com.huertohogar.data.repository.UserPreferencesRepository
import com.huertohogar.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.regex.Pattern

//nuestras Clases Selladas para el manejo de los estados de la UI de autenticacion
sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: String) : AuthUiState()
    object LoggedOut : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

/**
 * nuestro ViewModel para  el manejo de la logica de Login, Registro y Cierre de Sesion.
 */
class AuthViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    // Estado de sesion que se lee directamente de DataStore
    val isLoggedIn: StateFlow<Boolean> = preferencesRepository.isLoggedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val currentUserEmail: StateFlow<String?> = preferencesRepository.userEmail.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // Validacion de email
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
        )
        return emailPattern.matcher(email).matches()
    }

    // Validacion de contraseña con restriccion de un minimo de 6 caracteres.
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    // Nuestra función de login con validación
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            // Realizamos validaciones
            if (email.isBlank()) {
                _uiState.value = AuthUiState.Error("El correo electrónico es requerido")
                return@launch
            }

            if (!isValidEmail(email)) {
                _uiState.value = AuthUiState.Error("El formato del correo electrónico no es válido")
                return@launch
            }

            if (password.isBlank()) {
                _uiState.value = AuthUiState.Error("La contraseña es requerida")
                return@launch
            }

            // Intenta el login con UserRepository
            val user = userRepository.loginUser(email, password)
            if (user != null) {
                preferencesRepository.saveLoginState(email)
                _uiState.value = AuthUiState.Success(email)
            } else {
                _uiState.value = AuthUiState.Error("Credenciales incorrectas. Intente de nuevo.")
            }
        }
    }

    // Función de registro con validaciones
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            // Realizamos validaciones
            if (name.isBlank()) {
                _uiState.value = AuthUiState.Error("El nombre es requerido")
                return@launch
            }

            if (name.length < 3) {
                _uiState.value = AuthUiState.Error("El nombre debe tener al menos 3 caracteres")
                return@launch
            }

            if (email.isBlank()) {
                _uiState.value = AuthUiState.Error("El correo electrónico es requerido")
                return@launch
            }

            if (!isValidEmail(email)) {
                _uiState.value = AuthUiState.Error("El formato del correo electrónico no es válido")
                return@launch
            }

            if (password.isBlank()) {
                _uiState.value = AuthUiState.Error("La contraseña es requerida")
                return@launch
            }

            if (!isValidPassword(password)) {
                _uiState.value = AuthUiState.Error("La contraseña debe tener al menos 6 caracteres")
                return@launch
            }

            // Creamos un usuario y lo guardamos en la BD
            val newUser = User(
                email = email,
                password = password, // En producción debería estar hasheado
                fullName = name
            )

            val success = userRepository.registerUser(newUser)
            if (success) {
                preferencesRepository.saveLoginState(email)
                _uiState.value = AuthUiState.Success(email)
            } else {
                _uiState.value = AuthUiState.Error("El correo electrónico ya está registrado")
            }
        }
    }

    // Función logout para cerrar sesión
    fun logout() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            preferencesRepository.clearLoginState()
            _uiState.value = AuthUiState.LoggedOut
        }
    }

    // Función para resetear el estado de la UI
    fun resetUiState() {
        _uiState.value = AuthUiState.Idle
    }

    // Factory para instanciar el ViewModel
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                val database = AppDatabase.getDatabase(application)
                val userDao = database.userDao()
                val preferencesRepo = UserPreferencesRepository(application.applicationContext)
                val userRepository = UserRepository(userDao)

                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(preferencesRepo, userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}