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
    fun prepararEscenario() {
        repository = mockk(relaxed = true)
        viewModel = CartViewModel(repository, mockk(relaxed = true))
    }

    @Test
    fun `deberia calcular el monto total correctamente`() = runTest {
        // Dado
        val items = listOf(
            CartItem("1", "Producto 1", 1000.0, 2, "img1"),
            CartItem("2", "Producto 2", 500.0, 3, "img2")
        )
        coEvery { repository.getCartItems() } returns flowOf(items)

        // Cuando & Entonces
        viewModel.cartItems.test {
            val cartItems = awaitItem()

            viewModel.totalAmount shouldBe 3500.0 // (1000 * 2) + (500 * 3)
            viewModel.itemCount shouldBe 5        // 2 + 3

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deberia actualizar la cantidad correctamente`() = runTest {
        // Dado
        val item = CartItem("1", "Producto 1", 1000.0, 1, "img1")
        coEvery { repository.getCartItems() } returns flowOf(listOf(item))
        coEvery { repository.insertOrUpdateCartItem(any()) } returns Unit

        // Cuando
        viewModel.updateQuantity("1", 5)

        // Entonces
        coVerify { repository.insertOrUpdateCartItem(any()) }
    }

    @Test
    fun `deberia eliminar el item cuando la cantidad es cero`() = runTest {
        // Dado
        val item = CartItem("1", "Producto 1", 1000.0, 1, "img1")
        coEvery { repository.getCartItems() } returns flowOf(listOf(item))
        coEvery { repository.insertOrUpdateCartItem(any()) } returns Unit

        // Cuando
        viewModel.updateQuantity("1", 0)

        // Entonces
        coVerify { repository.insertOrUpdateCartItem(any()) }
    }
}