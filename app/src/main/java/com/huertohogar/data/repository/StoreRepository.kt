package com.huertohogar.data.repository

import com.huertohogar.data.model.StoreLocation
import com.huertohogar.data.model.StoreLocationDao
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para gestión de ubicaciones de tiendas
 */
class StoreRepository(
    private val storeLocationDao: StoreLocationDao
) {
    fun getAllStoreLocations(): Flow<List<StoreLocation>> {
        return storeLocationDao.getAllStoreLocations()
    }

    fun getStoresByCity(city: String): Flow<List<StoreLocation>> {
        return storeLocationDao.getStoresByCity(city)
    }
}
