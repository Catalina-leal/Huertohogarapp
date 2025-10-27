package com.huertohogar.huertohogarapp.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    // Variables reactivas para estado de autenticación
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var isLoggedIn = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    private var sharedPreferences: SharedPreferences? = null

    // Inicializa el almacenamiento local
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("HuertoPrefs", Context.MODE_PRIVATE)
        isLoggedIn.value = sharedPreferences?.getBoolean("isLoggedIn", false) ?: false
    }

    // Registro de usuario (almacena localmente)
    fun register(name: String, email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            errorMessage.value = "Completa todos los campos"
            return false
        }

        val editor = sharedPreferences?.edit()
        editor?.putString("user_name", name)
        editor?.putString("user_email", email)
        editor?.putString("user_password", password)
        editor?.apply()

        errorMessage.value = ""
        return true
    }

    // Iniciar sesión
    fun login(inputEmail: String, inputPassword: String): Boolean {
        val savedEmail = sharedPreferences?.getString("user_email", null)
        val savedPassword = sharedPreferences?.getString("user_password", null)

        return if (inputEmail == savedEmail && inputPassword == savedPassword) {
            isLoggedIn.value = true
            sharedPreferences?.edit()?.putBoolean("isLoggedIn", true)?.apply()
            true
        } else {
            errorMessage.value = "Credenciales incorrectas"
            false
        }
    }

    // Cerrar sesión
    fun logout() {
        isLoggedIn.value = false
        sharedPreferences?.edit()?.putBoolean("isLoggedIn", false)?.apply()
    }
}

