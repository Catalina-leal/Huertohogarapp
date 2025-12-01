package com.huertohogar.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// aca inicializamos DataStore a nivel de aplicacion osea (Singleton implicito)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(context: Context) {

    private val dataStore = context.dataStore

    // las Claves para el almacenamiento del estado de la sesión
    private object PreferencesKeys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    // Obtenemos el estado de logueo
    // Devolvemos un Flow<Boolean> observado por el ViewModel
    val isLoggedIn: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
        }

    // Obtenemos el correo del usuario actual
    val userEmail: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_EMAIL]
        }

    // Guardamos el estado del login y su correo
    suspend fun saveLoginState(email: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = true
            preferences[PreferencesKeys.USER_EMAIL] = email
        }
    }

    // borra el estado de sesion osea el logout
    suspend fun clearLoginState() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = false
            preferences.remove(PreferencesKeys.USER_EMAIL)
        }
    }
}