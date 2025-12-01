package com.huertohogar.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name")
    fun getAllProducts(): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE isActive = 1 ORDER BY name")
    fun getActiveProducts(): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: String): Product?
    
    @Query("SELECT * FROM products WHERE category = :category")
    fun getProductsByCategory(category: String): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'")
    fun searchProducts(searchQuery: String): Flow<List<Product>>
    
    @Query("SELECT * FROM products WHERE isOrganic = 1")
    fun getOrganicProducts(): Flow<List<Product>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)
    
    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int
    
    @Update
    suspend fun update(product: Product)
    
    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteById(productId: String)
}
