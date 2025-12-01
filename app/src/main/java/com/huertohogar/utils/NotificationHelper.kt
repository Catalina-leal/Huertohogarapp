package com.huertohogar.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.huertohogar.R
import com.huertohogar.data.model.OrderStatus

object NotificationHelper {
    private const val CHANNEL_ID = "huertohogar_orders"
    private const val CHANNEL_NAME = "Pedidos HuertoHogar"

    fun createNotificationChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones sobre el estado de tus pedidos"
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showOrderStatusNotification(
        context: Context,
        orderId: String,
        status: OrderStatus,
        title: String = "Actualización de Pedido"
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val message = when (status) {
            OrderStatus.CONFIRMED -> "Tu pedido #${orderId.take(8)} ha sido confirmado"
            OrderStatus.PREPARING -> "Tu pedido está siendo preparado"
            OrderStatus.SHIPPED -> "Tu pedido ha sido enviado"
            OrderStatus.IN_TRANSIT -> "Tu pedido está en camino"
            OrderStatus.DELIVERED -> "¡Tu pedido ha sido entregado!"
            OrderStatus.CANCELLED -> "Tu pedido ha sido cancelado"
            else -> "Estado actualizado: ${status.name}"
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(orderId.hashCode(), notification)
    }
}
