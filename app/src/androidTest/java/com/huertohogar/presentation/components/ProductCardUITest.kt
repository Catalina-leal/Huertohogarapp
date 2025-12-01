package com.huertohogar.presentation.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huertohogar.data.model.Product
import com.huertohogar.data.model.ProductCategory
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductCardUITest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun productCard_displaysProductName() {
        // Given
        val product = Product(
            id = "1",
            name = "Manzana Fuji",
            description = "Test",
            price = 1200.0,
            stock = 10.0,
            category = ProductCategory.FRUTAS_FRESCAS,
            imageUrlName = "test"
        )
        
        // When
        composeTestRule.setContent {
            ProductCard(product = product)
        }
        
        // Then
        composeTestRule.onNodeWithText("Manzana Fuji").assertExists()
    }
    
    @Test
    fun productCard_displaysPrice() {
        // Given
        val product = Product(
            id = "1",
            name = "Manzana",
            description = "Test",
            price = 1200.0,
            stock = 10.0,
            category = ProductCategory.FRUTAS_FRESCAS,
            imageUrlName = "test",
            unit = "kg"
        )
        
        // When
        composeTestRule.setContent {
            ProductCard(product = product)
        }
        
        // Then
        composeTestRule.onNodeWithText("$1200 / kg", substring = true).assertExists()
    }
    
    @Test
    fun productCard_callsOnAddToCart_whenAddButtonClicked() {
        // Given
        var addToCartCalled = false
        val product = Product(
            id = "1",
            name = "Manzana",
            description = "Test",
            price = 1200.0,
            stock = 10.0,
            category = ProductCategory.FRUTAS_FRESCAS,
            imageUrlName = "test"
        )
        
        // When
        composeTestRule.setContent {
            ProductCard(
                product = product,
                onAddToCart = { addToCartCalled = true }
            )
        }
        
        composeTestRule.onNodeWithText("Añadir").performClick()
        
        // Then
        assert(addToCartCalled)
    }
    
    @Test
    fun productCard_displaysTag() {
        // Given
        val product = Product(
            id = "1",
            name = "Manzana",
            description = "Test",
            price = 1200.0,
            stock = 10.0,
            category = ProductCategory.FRUTAS_FRESCAS,
            imageUrlName = "test",
            tag = "Frescos"
        )
        
        // When
        composeTestRule.setContent {
            ProductCard(product = product)
        }
        
        // Then
        composeTestRule.onNodeWithText("Frescos").assertExists()
    }
}
