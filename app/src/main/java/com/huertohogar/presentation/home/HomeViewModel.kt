package com.huertohogar.presentation.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.db.AppDatabase
import com.huertohogar.data.model.CartItem
import com.huertohogar.data.model.Product
import com.huertohogar.data.model.Testimonial
import com.huertohogar.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//Definicion de HomeUiState, necesario para resolver errores en HomeScreen
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val products: List<Product>,
        val testimonials: List<Testimonial>
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}


class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    // manejo de estados
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    init {
        // Observa la base de datos local
        viewModelScope.launch {
            repository.getCartItems().collect { items ->
                _cartItems.value = items
            }
        }
        // Carga de datos de la red/repositorio
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                // Cargamos testimonios una sola vez
                val testimonials = repository.getTestimonials()
                
                // Observamos productos y actualizamos solo los productos
                repository.getProducts().collect { products ->
                    _uiState.value = HomeUiState.Success(products, testimonials)
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Fallo la conexión: ${e.message}")
            }
        }
    }

    fun addToCart(item: CartItem) = viewModelScope.launch {
        repository.insertOrUpdateCartItem(item)
    }

    fun clearCart() = viewModelScope.launch {
        repository.clearCart()
    }


    // factory para la instancia del ViewModel con dependencias
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {

                // Obtenemos los DAOs a través del singleton de la base de datos
                val database = AppDatabase.getDatabase(application)
                val cartDao = database.cartDao()
                val productDao = database.productDao()

                // Creamos el Repositorio con los DAOs
                val repository = HomeRepository(cartDao, productDao)

                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}