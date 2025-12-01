package com.huertohogar.presentation.checkout

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.db.AppDatabase
import com.huertohogar.data.model.CartItem
import com.huertohogar.data.model.OrderStatus
import com.huertohogar.presentation.cart.CartViewModel
import com.huertohogar.data.repository.OrderRepository
import com.huertohogar.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val orderCreated: Boolean = false,
    val error: String? = null,
    val orderId: String? = null
)

class CheckoutViewModel(
    private val cartViewModel: CartViewModel,
    private val orderRepository: OrderRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    val cartItems: StateFlow<List<CartItem>> = cartViewModel.cartItems
    val totalAmount: Double // <-- Corregido aquí: se añadió el tipo Double
        get() = cartViewModel.totalAmount

    fun createOrder(
        shippingAddress: String,
        city: String,
        region: String,
        deliveryDate: Long? = null
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val userEmail = preferencesRepository.userEmail.first() // Corrected: using .first()
                if (userEmail == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Debes iniciar sesión para realizar un pedido"
                    )
                    return@launch
                }

                val items = cartItems.value
                if (items.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "El carrito está vacío"
                    )
                    return@launch
                }

                val fullAddress = "$shippingAddress, $city, $region"
                val order = orderRepository.createOrder(
                    userEmail = userEmail, // Corrected: using the variable userEmail
                    items = items,
                    shippingAddress = fullAddress,
                    deliveryDate = deliveryDate
                )

                // Limpiamos carrito después de crear el pedido
                cartViewModel.clearCart()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    orderCreated = true,
                    orderId = order.orderId
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al crear el pedido: ${e.message}"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = CheckoutUiState()
    }

    class Factory(
        private val application: Application,
        private val cartViewModel: CartViewModel
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
                val database = AppDatabase.getDatabase(application)
                val orderDao = database.orderDao()
                val orderItemDao = database.orderItemDao()
                val orderRepository = OrderRepository(orderDao, orderItemDao)
                val preferencesRepository = UserPreferencesRepository(application.applicationContext)

                @Suppress("UNCHECKED_CAST")
                return CheckoutViewModel(cartViewModel, orderRepository, preferencesRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
