package com.huertohogar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Nuestro modelo de ubicaciones de las tiendas para el mapa
 */
@Entity(tableName = "store_locations")
data class StoreLocation(
    @PrimaryKey
    val id: String,
    val name: String,
    val city: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val phone: String = "",
    val hours: String = "" // Horario de atención
)
