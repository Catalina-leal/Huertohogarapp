package com.huertohogar.data.repository

import com.huertohogar.data.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para gestión de productos
 */
class ProductRepository(
    private val productDao: ProductDao
) {
    fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts()
    }
    
    fun getActiveProducts(): Flow<List<Product>> {
        return productDao.getActiveProducts()
    }

    suspend fun getProductById(productId: String): Product? {
        return productDao.getProductById(productId)
    }

    fun getProductsByCategory(category: ProductCategory): Flow<List<Product>> {
        return productDao.getProductsByCategory(category.name)
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query)
    }

    fun getOrganicProducts(): Flow<List<Product>> {
        return productDao.getOrganicProducts()
    }
    
    suspend fun insertProduct(product: Product) {
        productDao.insert(product)
    }
    
    suspend fun updateProduct(product: Product) {
        productDao.update(product)
    }
    
    suspend fun deleteProduct(productId: String) {
        productDao.deleteById(productId)
    }
}
