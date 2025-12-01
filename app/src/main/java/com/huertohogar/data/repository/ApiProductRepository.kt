package com.huertohogar.data.repository

import com.huertohogar.data.api.ApiClient
import com.huertohogar.data.api.ProductApiResponse
import com.huertohogar.data.model.Product
import com.huertohogar.data.model.ProductCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * en este repositorio  combinamos todos los datos locales (Room) con API backend
 *ejemplo cuando la API está disponible, automaticamente usa los datos del servidor
 * y Cuando no, usa los datos locales
 */
class ApiProductRepository(
    private val localRepository: ProductRepository
) {
    
    suspend fun getProducts(): Flow<List<Product>> = flow {
        if (ApiClient.isApiAvailable()) {
            try {
                val response = ApiClient.apiService.getProducts()
                if (response.isSuccessful && response.body() != null) {
                    val apiProducts = response.body()!!
                    val products = apiProducts.map { it.toProduct() }
                    emit(products)
                } else {
                    // esta haciendo un fallback a los datos locales
                    localRepository.getAllProducts().collect { emit(it) }
                }
            } catch (e: Exception) {
                // Fallback a los datos locales en caso de error
                localRepository.getAllProducts().collect { emit(it) }
            }
        } else {
            // Uso de los datos locales
            localRepository.getAllProducts().collect { emit(it) }
        }
    }
    
    suspend fun getProductById(productId: String): Product? {
        if (ApiClient.isApiAvailable()) {
            try {
                val response = ApiClient.apiService.getProductById(productId)
                if (response.isSuccessful && response.body() != null) {
                    return response.body()!!.toProduct()
                }
            } catch (e: Exception) {
                // Fallback al local
            }
        }
        return localRepository.getProductById(productId)
    }
}

// Esta es una extensión para convertir ProductApiResponse a Product
private fun ProductApiResponse.toProduct(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        price = price,
        oldPrice = null,
        stock = stock,
        category = when (category) {
            "FRUTAS_FRESCAS" -> ProductCategory.FRUTAS_FRESCAS
            "VERDURAS_ORGANICAS" -> ProductCategory.VERDURAS_ORGANICAS
            "PRODUCTOS_ORGANICOS" -> ProductCategory.PRODUCTOS_ORGANICOS
            "PRODUCTOS_LACTEOS" -> ProductCategory.PRODUCTOS_LACTEOS
            else -> ProductCategory.FRUTAS_FRESCAS
        },
        imageUrlName = imageUrl,
        tag = if (isOrganic) "Orgánico" else "Frescos",
        origin = origin ?: "",
        unit = "kg",
        isOrganic = isOrganic,
        certifications = if (isOrganic) "Certificación Orgánica" else "",
        sustainablePractices = ""
    )
}
