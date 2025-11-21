package com.huertohogar.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    /**
     * aca Devolvemos una lista reactiva (Flow) de todos los ítems en el carrito.
     */
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItem>>

    /**
     * esta linea Inserta un nuevo ítem o reemplaza uno existente (si el productId coincide).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItem)

    /**
     * Actualizamos  un ítem existente.
     */
    @Update
    suspend fun update(item: CartItem)

    /**
     * Eliminamos un ítem específico del carrito por su ID de producto.
     */
    @Query("DELETE FROM cart_items WHERE product_id = :productId")
    suspend fun deleteItemById(productId: String)

    /**
     * Eliminamos todos los ítems del carrito.
     */
    @Query("DELETE FROM cart_items")
    suspend fun clearAll()
}