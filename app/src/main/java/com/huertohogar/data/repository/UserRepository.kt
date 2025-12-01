package com.huertohogar.data.repository

import com.huertohogar.data.model.User
import com.huertohogar.data.model.UserDao
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para gestión de usuarios
 */
class UserRepository(
    private val userDao: UserDao
) {
    suspend fun registerUser(user: User): Boolean {
        return try {
            // Verificamos si el usuario ya existe
            val existingUser = userDao.getUserByEmail(user.email)
            if (existingUser != null) {
                false // devuelve false cuando el usuario ya existe
            } else {
                userDao.insertUser(user)
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun loginUser(email: String, password: String): User? {
        val user = userDao.getUserByEmail(email)
        return if (user != null && user.password == password) {
            user
        } else {
            null
        }
    }

    fun getUser(email: String): Flow<User?> {
        return userDao.getUserByEmailFlow(email)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun updateLoyaltyPoints(email: String, points: Int) {
        userDao.updateLoyaltyPoints(email, points)
    }
    
    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }
    
    suspend fun deleteUser(email: String) {
        userDao.deleteUser(email)
    }
}
