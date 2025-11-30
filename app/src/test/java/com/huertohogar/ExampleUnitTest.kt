package com.huertohogar
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ExampleUnitTest {

    @Test
    fun `deberia sumar correctamente`() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `ejemplo con mockk`() {
        // Dado
        val dependency = mockk<List<String>>()
        every { dependency.size } returns 5

        // Cuando
        val result = dependency.size

        // Entonces
        assertEquals(5, result)
        verify { dependency.size }
    }
}