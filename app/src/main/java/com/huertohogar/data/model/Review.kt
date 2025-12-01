package com.huertohogar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Nuestro modelo de Reseña/ osea Calificacion de producto
 */
@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: String,
    val userEmail: String,
    val userName: String,
    val rating: Int, // 1-5 estrellas
    val comment: String,
    val createdAt: Long = System.currentTimeMillis()
)
