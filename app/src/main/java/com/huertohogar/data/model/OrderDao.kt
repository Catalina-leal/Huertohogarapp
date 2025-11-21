package com.huertohogar.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE userEmail = :userEmail ORDER BY orderDate DESC")
    fun getOrdersByUser(userEmail: String): Flow<List<Order>>
    
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getOrderById(orderId: String): Order?
    
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    fun getOrderByIdFlow(orderId: String): Flow<Order?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)
    
    @Update
    suspend fun updateOrder(order: Order)
    
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getAllOrders(): Flow<List<Order>>
    
    @Query("SELECT SUM(totalAmount) FROM orders WHERE status = 'DELIVERED'")
    suspend fun getTotalSales(): Double?
    
    @Query("SELECT COUNT(*) FROM orders")
    suspend fun getTotalOrdersCount(): Int
    
    @Query("SELECT COUNT(*) FROM orders WHERE status = 'DELIVERED'")
    suspend fun getDeliveredOrdersCount(): Int
}
