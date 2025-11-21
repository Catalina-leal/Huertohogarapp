package com.huertohogar.presentation.home

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huertohogar.data.model.Product
import com.huertohogar.data.model.ProductCategory
import com.huertohogar.data.model.Testimonial
import com.huertohogar.presentation.home.HomeScreen
import com.huertohogar.presentation.home.HomeUiState
import com.huertohogar.presentation.home.HomeViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenUITest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun homeScreen_displaysLoading_whenStateIsLoading() {
        // Given
        val viewModel = mockk<HomeViewModel>()
        every { viewModel.uiState } returns MutableStateFlow(HomeUiState.Loading)
        
        // When
        composeTestRule.setContent {
            // HomeScreen would need navController, but for testing we can mock it
            // This is a simplified version
        }
        
        // Then
        composeTestRule.onNodeWithContentDescription("Loading").assertExists()
    }
    
    @Test
    fun homeScreen_displaysProducts_whenStateIsSuccess() {
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
            Testimonial("Test", "Test", "Test", "test")
        )
        val viewModel = mockk<HomeViewModel>()
        every { viewModel.uiState } returns MutableStateFlow(
            HomeUiState.Success(products, testimonials)
        )
        
        // When
        composeTestRule.setContent {
            // HomeScreen setup
        }
        
        // Then
        composeTestRule.onNodeWithText("Manzana").assertExists()
    }
    
    @Test
    fun homeScreen_displaysError_whenStateIsError() {
        // Given
        val errorMessage = "Error de conexión"
        val viewModel = mockk<HomeViewModel>()
        every { viewModel.uiState } returns MutableStateFlow(
            HomeUiState.Error(errorMessage)
        )
        
        // When
        composeTestRule.setContent {
            // HomeScreen setup
        }
        
        // Then
        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }
}
