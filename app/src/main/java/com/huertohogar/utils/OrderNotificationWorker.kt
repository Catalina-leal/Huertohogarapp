package com.huertohogar.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.huertohogar.data.api.ApiClient
import com.huertohogar.data.api.OrderApiResponse
import com.huertohogar.data.model.OrderStatus
import kotlinx.coroutines.flow.first

/**
 * Worker para verificar cambios en el estado de pedidos y enviar notificaciones
 */
class OrderNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Obtenemos email del usuario desde DataStore
            val userEmail = inputData.getString("user_email")
            
            if (userEmail != null && ApiClient.isApiAvailable()) {
                // Obtenemos pedidos del usuario desde la API
                val response = ApiClient.apiService.getOrdersByUser(userEmail)
                
                if (response.isSuccessful && response.body() != null) {
                    val orders = response.body()!!
                    
                    // Verificamos cambios de estado y enviamos notificaciones
                    orders.forEach { order ->
                        val status = OrderStatus.valueOf(order.status)
                        if (status != OrderStatus.DELIVERED && status != OrderStatus.CANCELLED) {
                            // Enviar notificación si hay cambio de estado
                            NotificationHelper.showOrderStatusNotification(
                                applicationContext,
                                order.orderId,
                                status
                            )
                        }
                    }
                }
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
