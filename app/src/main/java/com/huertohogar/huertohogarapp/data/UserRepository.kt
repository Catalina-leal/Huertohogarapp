package com.huertohogar.huertohogarapp.data
import android.content.Context
import android.content.SharedPreferences

class UserRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences("huerto_prefs", Context.MODE_PRIVATE)

    fun saveUser(name: String, email: String, pass: String) {
        prefs.edit()
            .putString("name", name)
            .putString("email", email)
            .putString("pass", pass)
            .apply()
    }

    fun checkLogin(email: String, pass: String): Boolean {
        val savedEmail = prefs.getString("email", null)
        val savedPass = prefs.getString("pass", null)
        return (email == savedEmail && pass == savedPass)
    }

    fun getUserName(): String {
        return prefs.getString("name", "Usuario") ?: "Usuario"
    }
}
