package com.huertohogar.presentation.admin

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.db.AppDatabase
import com.huertohogar.data.model.Order
import com.huertohogar.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminSalesUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class SalesStats(
    val totalSales: Double = 0.0,
    val totalOrders: Int = 0,
    val deliveredOrders: Int = 0,
    val averageOrderValue: Double = 0.0
)

class AdminSalesViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminSalesUiState(isLoading = true))
    val uiState: StateFlow<AdminSalesUiState> = _uiState.asStateFlow()

    private val _salesStats = MutableStateFlow(SalesStats())
    val salesStats: StateFlow<SalesStats> = _salesStats.asStateFlow()

    fun loadSalesData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // Cargamos todas las órdenes
                orderRepository.getAllOrders().collect { orders ->
                    val totalSales = orderRepository.getTotalSales()
                    val totalOrders = orderRepository.getTotalOrdersCount()
                    val deliveredOrders = orderRepository.getDeliveredOrdersCount()
                    val averageOrderValue = if (totalOrders > 0) totalSales / totalOrders else 0.0

                    _salesStats.value = SalesStats(
                        totalSales = totalSales,
                        totalOrders = totalOrders,
                        deliveredOrders = deliveredOrders,
                        averageOrderValue = averageOrderValue
                    )

                    _uiState.value = _uiState.value.copy(
                        orders = orders.take(50), // Mostramos solo los últimos 50
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AdminSalesViewModel::class.java)) {
                val database = AppDatabase.getDatabase(application)
                val orderDao = database.orderDao()
                val orderItemDao = database.orderItemDao()
                val repository = OrderRepository(orderDao, orderItemDao)
                @Suppress("UNCHECKED_CAST")
                return AdminSalesViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
