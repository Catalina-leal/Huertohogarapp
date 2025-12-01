package com.huertohogar.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.huertohogar.data.model.Product

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: ((Product) -> Unit)? = null,
    onViewDetails: ((Product) -> Unit)? = null
) {
    // Aquí manejamos la lógica para cargar las imágenes desde la carpeta Drawable
    val context = LocalContext.current
    val imageResId: Int = remember(product.imageUrlName) {
        try {
            val resId: Int = context.resources.getIdentifier(
                product.imageUrlName,
                "drawable",
                context.packageName
            )
            // Verificamos que el recurso existe y es válido
            if (resId != 0) {
                try {
                    // Verificamos que el recurso es realmente un drawable válido
                    val drawable = context.resources.getDrawable(resId, context.theme)
                    if (drawable != null) resId else 0
                } catch (e: Exception) {
                    0
                }
            } else {
                0
            }
        } catch (e: Exception) {
            0
        }
    }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable { onViewDetails?.invoke(product) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        // Sombra de la tarjeta (shadow = 0.07 en tu CSS)
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // Bloque de Imagen y Etiqueta (position-relative)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.LightGray.copy(alpha = 0.1f)) // bg-light
            ) {
                // como manejamos implementacion de la imagen
                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(Color.LightGray)) {
                        Text(
                            "No Image",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    }
                }

                // Etiqueta 'Frescos'
                Text(
                    text = product.tag,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp) // m-4
                        .background(
                            MaterialTheme.colorScheme.secondary, // bg-secondary
                            RoundedCornerShape(4.dp) // rounded
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp), // py-1 px-3
                    style = MaterialTheme.typography.labelSmall
                )
            }

            // Bloque de Nombre y Precios (text-center p-4)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp), // p-4
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Nombre del producto
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Precios
                Row(verticalAlignment = Alignment.Bottom) {
                    // Precio actual
                    Text(
                        text = "$${String.format("%.0f", product.price)} / ${product.unit}",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    // Precio anterior (tachado) si existe
                    product.oldPrice?.let { oldPrice ->
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "$${String.format("%.0f", oldPrice)}",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall.copy(
                                textDecoration = TextDecoration.LineThrough
                            )
                        )
                    }
                }
            }

            // Bloque de Botones (d-flex border-top)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray) // border-top
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón 'Ver detalles'
                TextButton(
                    onClick = { onViewDetails?.invoke(product) },
                    modifier = Modifier.weight(1f), // w-50
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Filled.Visibility, contentDescription = "Ver detalles", tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(4.dp))
                    Text("Detalles", color = Color.Black)
                }

                // Divisor (border-end)
                HorizontalDivider(
                    color = Color.LightGray,
                    modifier = Modifier.width(1.dp).fillMaxHeight(0.8f)
                )

                // Botón 'Añadir al carrito'
                TextButton(
                    onClick = { onAddToCart?.invoke(product) },
                    modifier = Modifier.weight(1f), // w-50
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Añadir al carrito", tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(4.dp))
                    Text("Añadir", color = Color.Black)
                }
            }

        }
    }
}