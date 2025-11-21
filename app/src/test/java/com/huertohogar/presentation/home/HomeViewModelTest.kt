package com.huertohogar.presentation.home

import app.cash.turbine.test
import com.huertohogar.data.model.CartItem
import com.huertohogar.data.model.Product
import com.huertohogar.data.model.ProductCategory
import com.huertohogar.data.model.Testimonial
import com.huertohogar.data.repository.HomeRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.huertohogar.data.model.ProductDao


class HomeViewModelTest {
    
    private lateinit var repository: HomeRepository
    private lateinit var viewModel: HomeViewModel
    
    @BeforeEach
    fun setup() {
        repository = mockk()
        val productDao = mockk<ProductDao>()
        viewModel = HomeViewModel(repository)
    }
    
    @Test
    fun `should emit loading state initially`() = runTest {
        // Given
        coEvery { repository.getProducts() } returns flowOf(emptyList())
        coEvery { repository.getCartItems() } returns flowOf(emptyList())
        
        // When
        viewModel.uiState.test {
            // Then
            val state = awaitItem()
            state.shouldBeInstanceOf<HomeUiState.Loading>()
        }
    }
    
    @Test
    fun `should emit success state with products and testimonials`() = runTest {
        // Given
        val products = listOf(
            Product(
                id = "1",
                name = "Manzana",
                description = "Test",
                price = 1000.0,
                stock = 10.0,
                category = ProductCategory.FRUTAS_FRESCAS,
                imageUrlName = "test"
            )
        )
        val testimonials = listOf(
            Testimonial(
                quote = "Test",
                name = "Test",
                profession = "Test",
                imageUrlName = "test"
            )
        )
        
        coEvery { repository.getProducts() } returns flowOf(products)
        coEvery { repository.getTestimonials() } returns testimonials
        coEvery { repository.getCartItems() } returns flowOf(emptyList())
        
        // When
        viewModel.uiState.test {
            skipItems(1) // Skip loading
            val state = awaitItem()
            
            // Then
            state.shouldBeInstanceOf<HomeUiState.Success>()
            val successState = state as HomeUiState.Success
            successState.products shouldBe products
            successState.testimonials shouldBe testimonials
        }
    }
    
    @Test
    fun `should add item to cart`() = runTest {
        // Given
        val cartItem = CartItem(
            productId = "1",
            name = "Manzana",
            price = 1000.0,
            quantity = 1,
            imageUrl = "test"
        )
        
        coEvery { repository.insertOrUpdateCartItem(any()) } returns Unit
        coEvery { repository.getCartItems() } returns flowOf(listOf(cartItem))
        coEvery { repository.getProducts() } returns flowOf(emptyList())
        
        // When
        viewModel.addToCart(cartItem)
        
        // Then
        coVerify(exactly = 1) { repository.insertOrUpdateCartItem(cartItem) }
    }
    
    @Test
    fun `should clear cart`() = runTest {
        // Given
        coEvery { repository.clearCart() } returns Unit
        coEvery { repository.getCartItems() } returns flowOf(emptyList())
        coEvery { repository.getProducts() } returns flowOf(emptyList())
        
        // When
        viewModel.clearCart()
        
        // Then
        coVerify(exactly = 1) { repository.clearCart() }
    }
    
    @Test
    fun `should emit error state on exception`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { repository.getProducts() } throws Exception(errorMessage)
        coEvery { repository.getCartItems() } returns flowOf(emptyList())
        
        // When
        viewModel.uiState.test {
            skipItems(1) // Skip loading
            val state = awaitItem()
            
            // Then
            state.shouldBeInstanceOf<HomeUiState.Error>()
            val errorState = state as HomeUiState.Error
            errorState.message shouldBe "Fallo la conexión: $errorMessage"
        }
    }
}
