package com.huertohogar.presentation.login

import app.cash.turbine.test
import com.huertohogar.data.model.User
import com.huertohogar.data.repository.UserPreferencesRepository
import com.huertohogar.data.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthViewModelTest {

    private lateinit var preferencesRepository: UserPreferencesRepository
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: AuthViewModel

    @BeforeEach
    fun `preparar escenario`() {
        preferencesRepository = mockk()
        userRepository = mockk()
        viewModel = AuthViewModel(preferencesRepository, userRepository)
    }

    @Test
    fun `deberia iniciar sesion exitosamente con credenciales validas`() = runTest {
        val email = "test@test.com"
        val password = "password123"
        val user = User(email = email, password = password, fullName = "Test User")

        coEvery { userRepository.loginUser(email, password) } returns user
        coEvery { preferencesRepository.saveLoginState(email) } returns Unit
        every { preferencesRepository.isLoggedIn } returns flowOf(false)
        every { preferencesRepository.userEmail } returns flowOf(null)

        viewModel.login(email, password)

        viewModel.uiState.test {
            skipItems(1) // Skip Idle
            val state = awaitItem()
            state.shouldBeInstanceOf<AuthUiState.Success>()
            (state as AuthUiState.Success).user shouldBe email
        }

        coVerify { preferencesRepository.saveLoginState(email) }
    }

    @Test
    fun `deberia fallar inicio de sesion con credenciales invalidas`() = runTest {
        val email = "test@test.com"
        val password = "wrongpassword"

        coEvery { userRepository.loginUser(email, password) } returns null
        every { preferencesRepository.isLoggedIn } returns flowOf(false)
        every { preferencesRepository.userEmail } returns flowOf(null)

        viewModel.login(email, password)

        viewModel.uiState.test {
            skipItems(1) // Skip Idle
            val state = awaitItem()
            state.shouldBeInstanceOf<AuthUiState.Error>()
            (state as AuthUiState.Error).message shouldBe "Credenciales incorrectas. Intente de nuevo."
        }
    }

    @Test
    fun `deberia validar formato de correo`() = runTest {
        val invalidEmail = "invalid-email"
        val password = "password123"

        every { preferencesRepository.isLoggedIn } returns flowOf(false)
        every { preferencesRepository.userEmail } returns flowOf(null)

        viewModel.login(invalidEmail, password)

        viewModel.uiState.test {
            skipItems(1)
            val state = awaitItem()
            state.shouldBeInstanceOf<AuthUiState.Error>()
            (state as AuthUiState.Error).message shouldBe "El formato del correo electrónico no es válido"
        }
    }

    @Test
    fun `deberia validar longitud de la contraseña`() = runTest {
        val email = "test@test.com"
        val shortPassword = "12345"

        every { preferencesRepository.isLoggedIn } returns flowOf(false)
        every { preferencesRepository.userEmail } returns flowOf(null)

        viewModel.login(email, shortPassword)

        viewModel.uiState.test {
            skipItems(1)
            val state = awaitItem()
            state.shouldBeInstanceOf<AuthUiState.Error>()
            (state as AuthUiState.Error).message shouldBe "La contraseña debe tener al menos 6 caracteres"
        }
    }

    @Test
    fun `deberia registrar usuario exitosamente`() = runTest {
        val name = "Test User"
        val email = "test@test.com"
        val password = "password123"

        coEvery { userRepository.registerUser(any()) } returns true
        coEvery { preferencesRepository.saveLoginState(email) } returns Unit
        every { preferencesRepository.isLoggedIn } returns flowOf(false)
        every { preferencesRepository.userEmail } returns flowOf(null)

        viewModel.register(name, email, password)

        viewModel.uiState.test {
            skipItems(1) // Skip Idle
            val state = awaitItem()
            state.shouldBeInstanceOf<AuthUiState.Success>()
        }

        coVerify { userRepository.registerUser(any()) }
        coVerify { preferencesRepository.saveLoginState(email) }
    }

    @Test
    fun `deberia cerrar sesion exitosamente`() = runTest {
        coEvery { preferencesRepository.clearLoginState() } returns Unit
        every { preferencesRepository.isLoggedIn } returns flowOf(false)
        every { preferencesRepository.userEmail } returns flowOf(null)

        viewModel.logout()

        viewModel.uiState.test {
            skipItems(1)
            val state = awaitItem()
            state.shouldBeInstanceOf<AuthUiState.LoggedOut>()
        }

        coVerify { preferencesRepository.clearLoginState() }
    }
}