package com.huertohogar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Nuestro Modelo de Blog Post para contenido educativo
 */
@Entity(tableName = "blog_posts")
data class BlogPost(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val author: String,
    val imageUrlName: String,
    val publishedDate: Long = System.currentTimeMillis(),
    val category: String = "Alimentación Saludable", // Categoría del blog
    val readTime: Int = 5 // el tiempo de lectura en minutos
)
