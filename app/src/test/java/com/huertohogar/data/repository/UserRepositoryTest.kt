package com.huertohogar.data.repository

import com.huertohogar.data.model.User
import com.huertohogar.data.model.UserDao
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserRepositoryTest {

    private lateinit var userDao: UserDao
    private lateinit var repository: UserRepository

    @BeforeEach
    fun `preparar escenario`() {
        userDao = mockk(relaxed = true)
        repository = UserRepository(userDao)
    }

    @Test
    fun `deberia registrar usuario nuevo exitosamente`() = runTest {
        val user = User(email = "test@test.com", password = "123456", fullName = "Test User")

        coEvery { userDao.getUserByEmail(user.email) } returns null
        coEvery { userDao.insertUser(user) } returns Unit

        val result = repository.registerUser(user)

        result shouldBe true
        coVerify { userDao.insertUser(user) }
    }

    @Test
    fun `deberia fallar registro si usuario ya existe`() = runTest {
        val user = User(email = "test@test.com", password = "123456", fullName = "Test User")

        coEvery { userDao.getUserByEmail(user.email) } returns user

        val result = repository.registerUser(user)

        result shouldBe false
        coVerify(exactly = 0) { userDao.insertUser(user) }
    }

    @Test
    fun `deberia iniciar sesion exitosamente con credenciales validas`() = runTest {
        val user = User(email = "test@test.com", password = "123456", fullName = "Test User")

        coEvery { userDao.getUserByEmail(user.email) } returns user

        val result = repository.loginUser(user.email, user.password)

        result shouldBe user
    }

    @Test
    fun `deberia fallar inicio de sesion con credenciales invalidas`() = runTest {
        val user = User(email = "test@test.com", password = "123456", fullName = "Test User")

        coEvery { userDao.getUserByEmail(user.email) } returns user

        val result = repository.loginUser(user.email, "wrongpassword")

        result shouldBe null
    }

    @Test
    fun `deberia actualizar usuario`() = runTest {
        val user = User(email = "test@test.com", password = "123456", fullName = "Test User")

        repository.updateUser(user)

        coVerify { userDao.updateUser(user) }
    }

    @Test
    fun `deberia actualizar puntos de lealtad`() = runTest {
        val email = "test@test.com"
        val points = 50

        repository.updateLoyaltyPoints(email, points)

        coVerify { userDao.updateLoyaltyPoints(email, points) }
    }

    @Test
    fun `deberia eliminar usuario`() = runTest {
        val email = "test@test.com"

        repository.deleteUser(email)

        coVerify { userDao.deleteUser(email) }
    }
}