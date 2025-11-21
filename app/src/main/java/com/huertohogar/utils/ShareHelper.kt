package com.huertohogar.utils

import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import com.huertohogar.data.model.Product

object ShareHelper {
    
    fun shareProduct(context: Context, product: Product) {
        val shareText = """
            🌱 ¡Mira este producto de HuertoHogar!
            
            ${product.name}
            Precio: $${String.format("%.0f", product.price)} / ${product.unit}
            ${if (product.isOrganic) "✅ Certificado Orgánico" else ""}
            
            ${product.description}
            
            Descarga la app HuertoHogar para más productos frescos del campo.
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Producto de HuertoHogar: ${product.name}")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        context.startActivity(Intent.createChooser(intent, "Compartir producto"))
    }

    fun shareOrder(context: Context, orderId: String, totalAmount: Double) {
        val shareText = """
            🛒 ¡Acabo de realizar un pedido en HuertoHogar!
            
            Pedido #${orderId.take(8)}
            Total: $${String.format("%.0f", totalAmount)} CLP
            
            Productos frescos directamente del campo a tu mesa.
            
            Descarga la app HuertoHogar para más productos orgánicos.
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Mi pedido en HuertoHogar")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        context.startActivity(Intent.createChooser(intent, "Compartir pedido"))
    }

    fun shareApp(context: Context) {
        val shareText = """
            🌱 Descarga HuertoHogar - Productos frescos del campo a tu mesa
            
            • Productos orgánicos certificados
            • Entrega a domicilio
            • Apoyo a agricultores locales
            • Más de 6 años de experiencia
            
            ¡Únete a la comunidad de HuertoHogar!
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "HuertoHogar - Productos Orgánicos")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        context.startActivity(Intent.createChooser(intent, "Compartir app"))
    }
}
