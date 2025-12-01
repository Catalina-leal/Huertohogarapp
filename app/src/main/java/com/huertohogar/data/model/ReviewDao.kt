package com.huertohogar.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE productId = :productId ORDER BY createdAt DESC")
    fun getReviewsByProduct(productId: String): Flow<List<Review>>
    
    @Query("SELECT AVG(rating) FROM reviews WHERE productId = :productId")
    suspend fun getAverageRating(productId: String): Double?
    
    @Query("SELECT COUNT(*) FROM reviews WHERE productId = :productId")
    suspend fun getReviewCount(productId: String): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review)
    
    @Query("SELECT * FROM reviews WHERE userEmail = :userEmail ORDER BY createdAt DESC")
    fun getReviewsByUser(userEmail: String): Flow<List<Review>>
}
