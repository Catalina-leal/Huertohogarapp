package com.huertohogar.data.repository

import app.cash.turbine.test
import com.huertohogar.data.model.CartItem
import com.huertohogar.data.model.CartDao
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HomeRepositoryTest {
    
    private lateinit var cartDao: CartDao
    private lateinit var productDao: com.huertohogar.data.model.ProductDao
    private lateinit var repository: HomeRepository
    
    @BeforeEach
    fun setup() {
        cartDao = mockk()
        productDao = mockk()
        repository = HomeRepository(cartDao, productDao)
    }
    
    @Test
    fun `should get cart items from dao`() = runTest {
        // Given
        val items = listOf(
            CartItem("1", "Product 1", 1000.0, 1, "img1")
        )
        coEvery { cartDao.getAllCartItems() } returns flowOf(items)
        
        // When
        repository.getCartItems().test {
            val result = awaitItem()
            
            // Then
            result shouldBe items
        }
    }
    
    @Test
    fun `should insert cart item`() = runTest {
        // Given
        val item = CartItem("1", "Product 1", 1000.0, 1, "img1")
        coEvery { cartDao.insert(any()) } returns Unit
        
        // When
        repository.insertOrUpdateCartItem(item)
        
        // Then
        coVerify { cartDao.insert(item) }
    }
    
    @Test
    fun `should clear cart`() = runTest {
        // Given
        coEvery { cartDao.clearAll() } returns Unit
        
        // When
        repository.clearCart()
        
        // Then
        coVerify { cartDao.clearAll() }
    }
    
    @Test
    fun `should get initial products`() = runTest {
        // When
        val products = repository.getInitialProducts()
        
        // Then
        products.isNotEmpty() shouldBe true
        products.first().name.isNotEmpty() shouldBe true
    }
}
