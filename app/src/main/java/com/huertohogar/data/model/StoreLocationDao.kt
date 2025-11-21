package com.huertohogar.data.model

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreLocationDao {
    @Query("SELECT * FROM store_locations")
    fun getAllStoreLocations(): Flow<List<StoreLocation>>
    
    @Query("SELECT * FROM store_locations WHERE city = :city")
    fun getStoresByCity(city: String): Flow<List<StoreLocation>>
}
