package com.huertohogar.data.repository

import com.huertohogar.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Repositorio para gestión de pedidos
 */
class OrderRepository(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
) {
    suspend fun createOrder(
        userEmail: String,
        items: List<CartItem>,
        shippingAddress: String,
        deliveryDate: Long? = null
    ): Order {
        val orderId = UUID.randomUUID().toString()
        val totalAmount = items.sumOf { it.price * it.quantity }

        val order = Order(
            orderId = orderId,
            userEmail = userEmail,
            totalAmount = totalAmount,
            shippingAddress = shippingAddress,
            deliveryDate = deliveryDate,
            status = OrderStatus.CONFIRMED
        )

        orderDao.insertOrder(order)

        // aca insertamos los items del pedido
        val orderItems = items.map { cartItem ->
            OrderItem(
                orderId = orderId,
                productId = cartItem.productId,
                productName = cartItem.name,
                quantity = cartItem.quantity,
                unitPrice = cartItem.price,
                totalPrice = cartItem.price * cartItem.quantity
            )
        }
        orderItemDao.insertOrderItems(orderItems)

        return order
    }

    fun getOrdersByUser(userEmail: String): Flow<List<Order>> {
        return orderDao.getOrdersByUser(userEmail)
    }

    fun getOrderById(orderId: String): Flow<Order?> {
        return orderDao.getOrderByIdFlow(orderId)
    }

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus) {
        val order = orderDao.getOrderById(orderId)
        if (order != null) {
            val updatedOrder = order.copy(status = status)
            orderDao.updateOrder(updatedOrder)
        }
    }

    fun getOrderItems(orderId: String): Flow<List<OrderItem>> {
        return orderItemDao.getItemsByOrderId(orderId)
    }
    
    fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAllOrders()
    }
    
    suspend fun getTotalSales(): Double {
        return orderDao.getTotalSales() ?: 0.0
    }
    
    suspend fun getTotalOrdersCount(): Int {
        return orderDao.getTotalOrdersCount()
    }
    
    suspend fun getDeliveredOrdersCount(): Int {
        return orderDao.getDeliveredOrdersCount()
    }
}
