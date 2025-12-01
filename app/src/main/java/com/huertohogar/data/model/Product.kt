package com.huertohogar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Categorías de productos
 */
enum class ProductCategory {
    FRUTAS_FRESCAS,
    VERDURAS_ORGANICAS,
    PRODUCTOS_ORGANICOS,
    PRODUCTOS_LACTEOS
}

/**
 * Nuestro modelo de Producto completo con toda la informacion requerida
 */
@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val price: Double, // Precio en CLP osea en pesos chilenos
    val oldPrice: Double? = null, // Precio anterior para mostrar descuentos
    val stock: Double,
    val category: ProductCategory,
    val imageUrlName: String,
    val tag: String = "Frescos",
    val origin: String = "",
    val unit: String = "kg", // Unidad de medida (kg, bolsa, frasco, etc.)
    val isOrganic: Boolean = false, // Si es organico
    val certifications: String = "", // Certificaciones organicas
    val sustainablePractices: String = "", // Practicas sostenibles
    val isActive: Boolean = true // Si el producto está activo/pausado
)

/**
 * los Testimonios para la seccion de reseñas
 */
data class Testimonial(
    val quote: String,
    val name: String,
    val profession: String,
    val imageUrlName: String
)