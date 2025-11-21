package com.huertohogar.presentation.cart

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.db.AppDatabase
import com.huertohogar.data.model.CartItem
import com.huertohogar.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: HomeRepository,
    private val application: Application
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            try {
                repository.getCartItems().collect { items ->
                    _cartItems.value = items
                }
            } catch (e: Exception) {
                // Manejar errores sin crash
                android.util.Log.e("CartViewModel", "Error loading cart items", e)
                _cartItems.value = emptyList()
            }
        }
    }

    fun addToCart(productId: String, name: String, price: Double, imageUrl: String, quantity: Int = 1) {
        viewModelScope.launch {
            val existingItem = _cartItems.value.find { it.productId == productId }
            if (existingItem != null) {
                // Actualiza la cantidad si ya existe
                val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
                repository.insertOrUpdateCartItem(updatedItem)
            } else {
                // Agrega un nuevo item
                val newItem = CartItem(
                    productId = productId,
                    name = name,
                    price = price,
                    quantity = quantity,
                    imageUrl = imageUrl
                )
                repository.insertOrUpdateCartItem(newItem)
            }
        }
    }

    fun updateQuantity(productId: String, newQuantity: Int) {
        viewModelScope.launch {
            val item = _cartItems.value.find { it.productId == productId }
            if (item != null && newQuantity > 0) {
                val updatedItem = item.copy(quantity = newQuantity)
                repository.insertOrUpdateCartItem(updatedItem)
            }
        }
    }

    fun removeItem(productId: String) {
        viewModelScope.launch {
            val item = _cartItems.value.find { it.productId == productId }
            if (item != null) {
                repository.deleteCartItem(item)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    // Propiedades calculadas de forma segura
    val totalAmount: Double
        get() = try {
            _cartItems.value.sumOf { it.price * it.quantity }
        } catch (e: Exception) {
            0.0
        }

    val itemCount: Int
        get() = try {
            _cartItems.value.sumOf { it.quantity }
        } catch (e: Exception) {
            0
        }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                val database = AppDatabase.getDatabase(application)
                val cartDao = database.cartDao()
                val productDao = database.productDao()
                val repository = HomeRepository(cartDao, productDao)
                @Suppress("UNCHECKED_CAST")
                return CartViewModel(repository, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
