package com.huertohogar.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Nuestra entidad de Room para la representacion un ítem en el carrito de compras.
 */
@Entity(tableName = "cart_items")
data class CartItem(
    // Clave primaria: ID del producto, aseguramos que solo haya una fila por producto.
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    val productId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "price")
    val price: Double,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "image_url")
    val imageUrl: String
)