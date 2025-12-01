package com.huertohogar.presentation.productdetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.db.AppDatabase
import com.huertohogar.data.model.Product
import com.huertohogar.data.model.Review
import com.huertohogar.data.repository.ProductRepository
import com.huertohogar.data.repository.ReviewRepository
import com.huertohogar.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first // <-- ¡Importación añadida aquí!
import kotlinx.coroutines.launch

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProductDetailViewModel(
    private val productRepository: ProductRepository,
    private val reviewRepository: ReviewRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _averageRating = MutableStateFlow(0.0)
    val averageRating: StateFlow<Double> = _averageRating.asStateFlow()

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val product = productRepository.getProductById(productId)
                _product.value = product
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun loadReviews(productId: String) {
        viewModelScope.launch {
            reviewRepository.getReviewsByProduct(productId).collect { reviews ->
                _reviews.value = reviews
                _averageRating.value = reviewRepository.getAverageRating(productId)
            }
        }
    }

    fun addReview(productId: String, rating: Int, comment: String) {
        viewModelScope.launch {
            val userEmail = preferencesRepository.userEmail.first() // <-- ¡Corrección aplicada aquí!
            val userName = "Usuario" // En producción, se obtendria del perfil
            if (userEmail != null) {
                val review = Review(
                    productId = productId,
                    userEmail = userEmail,
                    userName = userName,
                    rating = rating,
                    comment = comment
                )
                reviewRepository.addReview(review)
                loadReviews(productId) // Recarga de reseñas
            }
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
                val database = AppDatabase.getDatabase(application)
                val productDao = database.productDao()
                val reviewDao = database.reviewDao()
                val productRepository = ProductRepository(productDao)
                val reviewRepository = ReviewRepository(reviewDao)
                val preferencesRepository = UserPreferencesRepository(application.applicationContext)

                @Suppress("UNCHECKED_CAST")
                return ProductDetailViewModel(
                    productRepository,
                    reviewRepository,
                    preferencesRepository
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
