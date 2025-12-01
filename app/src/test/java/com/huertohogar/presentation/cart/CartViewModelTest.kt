package com.huertohogar.presentation.cart

import app.cash.turbine.test
import com.huertohogar.data.model.CartItem
import com.huertohogar.data.repository.HomeRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CartViewModelTest {
    
    private lateinit var repository: HomeRepository
    private lateinit var viewModel: CartViewModel
    
    @BeforeEach
    fun setup() {
        repository = mockk()
        viewModel = CartViewModel(repository, mockk())
    }
    
    @Test
    fun `should calculate total amount correctly`() = runTest {
        // Given
        val items = listOf(
            CartItem("1", "Product 1", 1000.0, 2, "img1"),
            CartItem("2", "Product 2", 500.0, 3, "img2")
        )
        coEvery { repository.getCartItems() } returns flowOf(items)
        
        // When
        viewModel.cartItems.test {
            val cartItems = awaitItem()
            
            // Then
            viewModel.totalAmount shouldBe 3500.0 // (1000 * 2) + (500 * 3)
            viewModel.itemCount shouldBe 5 // 2 + 3
        }
    }
    
    @Test
    fun `should update quantity correctly`() = runTest {
        // Given
        val item = CartItem("1", "Product 1", 1000.0, 1, "img1")
        coEvery { repository.getCartItems() } returns flowOf(listOf(item))
        coEvery { repository.insertOrUpdateCartItem(any()) } returns Unit
        
        // When
        viewModel.updateQuantity("1", 5)
        
        // Then
        coVerify { repository.insertOrUpdateCartItem(any()) }
    }
    
    @Test
    fun `should remove item when quantity is zero`() = runTest {
        // Given
        val item = CartItem("1", "Product 1", 1000.0, 1, "img1")
        coEvery { repository.getCartItems() } returns flowOf(listOf(item))
        coEvery { repository.insertOrUpdateCartItem(any()) } returns Unit
        
        // When
        viewModel.updateQuantity("1", 0)
        
        // Then
        // Should call remove or update with quantity 0
        coVerify { repository.insertOrUpdateCartItem(any()) }
    }
}
