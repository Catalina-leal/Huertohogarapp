package com.huertohogar.presentation.cart

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.db.AppDatabase
import com.huertohogar.data.model.CartItem
import com.huertohogar.data.repository.HomeRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: HomeRepository,
    private val application: Application
) : ViewModel() {

    // Convertimos el Flow directamente a StateFlow usando stateIn
    val cartItems: StateFlow<List<CartItem>> = repository.getCartItems()
        .catch { exception ->
            // Manejamos errores sin crash
            android.util.Log.e("CartViewModel", "Error loading cart items", exception)
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addToCart(productId: String, name: String, price: Double, imageUrl: String, quantity: Int = 1) {
        viewModelScope.launch {
            val existingItem = cartItems.value.find { it.productId == productId }
            if (existingItem != null) {
                // Actualizamos la cantidad si ya existe
                val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
                repository.insertOrUpdateCartItem(updatedItem)
            } else {
                // Agregamos un nuevo item
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
            val item = cartItems.value.find { it.productId == productId }
            if (item != null && newQuantity > 0) {
                val updatedItem = item.copy(quantity = newQuantity)
                repository.insertOrUpdateCartItem(updatedItem)
            }
        }
    }

    fun removeItem(productId: String) {
        viewModelScope.launch {
            val item = cartItems.value.find { it.productId == productId }
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
            cartItems.value.sumOf { it.price * it.quantity }
        } catch (e: Exception) {
            0.0
        }

    val itemCount: Int
        get() = try {
            cartItems.value.sumOf { it.quantity }
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
