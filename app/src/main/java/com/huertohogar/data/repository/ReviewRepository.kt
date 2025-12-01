package com.huertohogar.data.repository

import com.huertohogar.data.model.Review
import com.huertohogar.data.model.ReviewDao
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para gestión de reseñas
 */
class ReviewRepository(
    private val reviewDao: ReviewDao
) {
    fun getReviewsByProduct(productId: String): Flow<List<Review>> {
        return reviewDao.getReviewsByProduct(productId)
    }

    suspend fun getAverageRating(productId: String): Double {
        return reviewDao.getAverageRating(productId) ?: 0.0
    }

    suspend fun getReviewCount(productId: String): Int {
        return reviewDao.getReviewCount(productId)
    }

    suspend fun addReview(review: Review) {
        reviewDao.insertReview(review)
    }

    fun getReviewsByUser(userEmail: String): Flow<List<Review>> {
        return reviewDao.getReviewsByUser(userEmail)
    }
}
