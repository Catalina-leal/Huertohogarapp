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
    fun setup() {
        preferencesRepository = mockk()
        userRepository = mockk()
        viewModel = AuthViewModel(preferencesRepository, userRepository)
    }
    
    @Test
    fun `should login successfully with valid credentials`() = runTest {
        // Given
        val email = "test@test.com"
        val password = "password123"
        val user = User(
            email = email,
            password = password,
            fullName = "Test User"
        )
        
        coEvery { userRepository.loginUser(email, password) } returns user
        coEvery { preferencesRepository.saveLoginState(email) } returns Unit
        every { preferencesRepository.isLoggedIn } returns flowOf(false)
        every { preferencesRepository.userEmail } returns flowOf(null)
        
        // When
        viewModel.login(email, password)
        
        // Then
        viewModel.uiState.test {
            skipItems(1) // Skip Idle
            val state = awaitItem()
            state.shouldBeInstanceOf<AuthUiState.Success>()
            (state as AuthUiState.Success).user shouldBe email
        }
        
        coVerify { preferencesRepository.saveLoginState(email) }
    }
    
    @Test
    fun `should fail login with invalid credentials`() = runTest {
        // Given
        val email = "test@test.com"
        val password = "wrongpassword"
        
        coEvery { userRepository.loginUser(email, password) } returns null
        every { preferencesRepository.isLoggedIn } returns flowOf(false)
        every { preferencesRepository.userEmail } returns flowOf(null)
        
        // When
        viewModel.login(email, password)
        
        // Then
        viewModel.uiState.test {
            skipItems(1) // Skip Idle
            val state = awaitItem()
            state.shouldBeInstanceOf<AuthUiState.Error>()
            (state as AuthUiState.Error).message shouldBe "Credenciales incorrectas. Intente de nuevo."
        }
    }
    
    @Test
    fun `should validate email format`() = runTest {
        // Given
        val invalidEmail = "invalid-email"
        val password = "password123"
        
        every { preferencesRepository.isLoggedIn } returns flowOf(false)
        every { preferencesRepository.userEmail } returns flowOf(null)
        
        // When
        viewModel.login(invalidEmail, password)
        
        // Then
        viewModel.uiState.test {
            skipItems(1)
            val state = awaitItem()
            state.shouldBeInstanceOf<AuthUiState.Error>()
            (state as AuthUiState.Error).message shouldBe "El formato del correo electrónico no es válido"
        }
    }
    
    @Test
    fun `should validate password length`() = runTest {
        // Given
        val email = "test@test.com"
        val shortPassword = "12345"
        
        every { preferencesRepository.isLoggedIn } returns flowOf(false)
        every { preferencesRepository.userEmail } returns flowOf(null)
        
        // When
        viewModel.login(email, shortPassword)
        
        // Then
        viewModel.uiState.test {
            skipItems(1)
            val state = awaitItem()
            state.shouldBeInstanceOf<AuthUiState.Error>()
            (state as AuthUiState.Error).message shouldBe "La contraseña debe tener al menos 6 caracteres"
        }
    }
    
    @Test
    fun `should register user successfully`() = runTest {
        // Given
        val name = "Test User"
        val email = "test@test.com"
        val password = "password123"
        
        coEvery { userRepository.registerUser(any()) } returns true
        coEvery { preferencesRepository.saveLoginState(email) } returns Unit
        every { preferencesRepository.isLoggedIn } returns flowOf(false)
        every { preferencesRepository.userEmail } returns flowOf(null)
        
        // When
        viewModel.register(name, email, password)
        
        // Then
        viewModel.uiState.test {
            skipItems(1) // Skip Idle
            val state = awaitItem()
            state.shouldBeInstanceOf<AuthUiState.Success>()
        }
        
        coVerify { userRepository.registerUser(any()) }
        coVerify { preferencesRepository.saveLoginState(email) }
    }
    
    @Test
    fun `should logout successfully`() = runTest {
        // Given
        coEvery { preferencesRepository.clearLoginState() } returns Unit
        every { preferencesRepository.isLoggedIn } returns flowOf(false)
        every { preferencesRepository.userEmail } returns flowOf(null)
        
        // When
        viewModel.logout()
        
        // Then
        viewModel.uiState.test {
            skipItems(1)
            val state = awaitItem()
            state.shouldBeInstanceOf<AuthUiState.LoggedOut>()
        }
        
        coVerify { preferencesRepository.clearLoginState() }
    }
}
