package com.huertohogar.presentation.products

import app.cash.turbine.test
import com.huertohogar.data.model.Product
import com.huertohogar.data.model.ProductCategory
import com.huertohogar.data.repository.ProductRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductsViewModelTest {

    private lateinit var repository: ProductRepository
    private lateinit var viewModel: ProductsViewModel

    @BeforeEach
    fun setup() {
        repository = mockk()
        viewModel = ProductsViewModel(repository)
    }

    @Test
    fun `should load products on init`() = runTest {
        val products = listOf(
            Product(
                id = "1",
                name = "Manzana",
                description = "Test",
                price = 1000.0,
                oldPrice = null,
                stock = 10.0,
                category = ProductCategory.FRUTAS_FRESCAS,
                imageUrlName = "test"
            )
        )
        coEvery { repository.getAllProducts() } returns flowOf(products)

        viewModel.uiState.test {
            val state = awaitItem()
            state.products shouldBe products
            state.isLoading shouldBe false
            state.error shouldBe null
        }
    }

    @Test
    fun `should filter products by category`() = runTest {
        val allProducts = listOf(
            Product(
                id = "1",
                name = "Manzana",
                description = "Test",
                price = 1000.0,
                oldPrice = null,
                stock = 10.0,
                category = ProductCategory.FRUTAS_FRESCAS,
                imageUrlName = "test"
            ),
            Product(
                id = "2",
                name = "Zanahoria",
                description = "Test",
                price = 900.0,
                oldPrice = null,
                stock = 10.0,
                category = ProductCategory.VERDURAS_ORGANICAS,
                imageUrlName = "test"
            )
        )
        val filteredProducts = listOf(allProducts[0])

        coEvery { repository.getAllProducts() } returns flowOf(allProducts)
        coEvery { repository.getProductsByCategory(ProductCategory.FRUTAS_FRESCAS) } returns flowOf(filteredProducts)

        viewModel.filterByCategory(ProductCategory.FRUTAS_FRESCAS)

        viewModel.uiState.test {
            val state = awaitItem()
            state.products shouldBe filteredProducts
        }
    }

    @Test
    fun `should search products`() = runTest {
        val query = "manzana"
        val searchResults = listOf(
            Product(
                id = "1",
                name = "Manzana",
                description = "Test",
                price = 1000.0,
                oldPrice = null,
                stock = 10.0,
                category = ProductCategory.FRUTAS_FRESCAS,
                imageUrlName = "test"
            )
        )

        coEvery { repository.searchProducts(query) } returns flowOf(searchResults)

        viewModel.searchProducts(query)

        viewModel.uiState.test {
            val state = awaitItem()
            state.products shouldBe searchResults
        }
    }
}
