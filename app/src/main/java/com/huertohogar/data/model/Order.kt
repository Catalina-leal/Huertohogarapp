package com.huertohogar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * aca manejamos los enums para los distintos estados de pedido
 */
enum class OrderStatus {
    PENDING,        // Pendiente
    CONFIRMED,      // Confirmado
    PREPARING,      // En preparación
    SHIPPED,        // Enviado
    IN_TRANSIT,     // En tránsito
    DELIVERED,      // Entregado
    CANCELLED       // Cancelado
}

/**
 * nuestro modelo de Pedido
 */
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey
    val orderId: String,
    val userEmail: String,
    val orderDate: Long = System.currentTimeMillis(),
    val status: OrderStatus = OrderStatus.PENDING,
    val totalAmount: Double,
    val shippingAddress: String,
    val deliveryDate: Long? = null, // Fecha preferida de entrega
    val trackingNumber: String? = null,
    val notes: String = ""
)

/**
 * Item de pedido (productos en un pedido)
 */
@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderId: String,
    val productId: String,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double
)
