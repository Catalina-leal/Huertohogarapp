package com.huertohogar.presentation.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.db.AppDatabase
import com.huertohogar.data.model.Order
import com.huertohogar.data.model.OrderItem
import com.huertohogar.data.repository.OrderRepository
import com.huertohogar.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first // <-- ¡Importación añadida aquí!
import kotlinx.coroutines.launch

data class OrderHistoryUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class OrderHistoryViewModel(
    private val orderRepository: OrderRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderHistoryUiState(isLoading = true))
    val uiState: StateFlow<OrderHistoryUiState> = _uiState.asStateFlow()

    private val _orderItems = MutableStateFlow<Map<String, List<OrderItem>>>(emptyMap())
    val orderItems: StateFlow<Map<String, List<OrderItem>>> = _orderItems.asStateFlow()

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val userEmail = preferencesRepository.userEmail.first() // <-- ¡Corrección aplicada aquí!
                if (userEmail != null) {
                    orderRepository.getOrdersByUser(userEmail).collect { orders ->
                        _uiState.value = _uiState.value.copy(
                            orders = orders,
                            isLoading = false
                        )
                        // Cargamos items para cada pedido
                        orders.forEach { order ->
                            orderRepository.getOrderItems(order.orderId).collect { items ->
                                _orderItems.value = _orderItems.value + (order.orderId to items)
                            }
                        }
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Debes iniciar sesión para ver tu historial"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun getOrderItems(orderId: String): List<OrderItem> {
        return _orderItems.value[orderId] ?: emptyList()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OrderHistoryViewModel::class.java)) {
                val database = AppDatabase.getDatabase(application)
                val orderDao = database.orderDao()
                val orderItemDao = database.orderItemDao()
                val orderRepository = OrderRepository(orderDao, orderItemDao)
                val preferencesRepository = UserPreferencesRepository(application.applicationContext)

                @Suppress("UNCHECKED_CAST")
                return OrderHistoryViewModel(orderRepository, preferencesRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
