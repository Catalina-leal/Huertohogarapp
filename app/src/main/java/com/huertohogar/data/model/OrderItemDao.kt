package com.huertohogar.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderItemDao {
    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getItemsByOrderId(orderId: String): Flow<List<OrderItem>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(item: OrderItem)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItem>)
}
