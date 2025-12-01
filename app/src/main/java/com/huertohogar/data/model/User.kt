package com.huertohogar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Nuestro modelo de Usuarios para manejar las autenticaciones y perfiles
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val email: String,
    val password: String, // cabe mencionar que segun investigacion aprendimos q en producción debería estar hasheado
    val fullName: String,
    val phone: String = "",
    val address: String = "",
    val city: String = "",
    val region: String = "",
    val postalCode: String = "",
    val loyaltyPoints: Int = 0, // los puntos de fidelizacion
    val role: String = "USER", // USER o ADMIN
    val isActive: Boolean = true, // Si el usuario está activo
    val createdAt: Long = System.currentTimeMillis()
)
