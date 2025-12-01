package com.huertohogar.presentation.weather

import app.cash.turbine.test
import com.huertohogar.data.api.WeatherApiResponse
import com.huertohogar.data.api.WeatherMain
import com.huertohogar.data.api.WeatherInfo
import com.huertohogar.data.repository.WeatherRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WeatherViewModelTest {
    
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var viewModel: WeatherViewModel
    
    @BeforeEach
    fun setup() {
        weatherRepository = mockk()
        viewModel = WeatherViewModel(weatherRepository)
    }
    
    @Test
    fun `should load weather successfully`() = runTest {
        // Given
        val expectedWeather = WeatherApiResponse(
            name = "Santiago",
            main = WeatherMain(
                temp = 20.5,
                feels_like = 19.0,
                temp_min = 15.0,
                temp_max = 25.0,
                humidity = 60,
                pressure = 1013
            ),
            weather = listOf(
                WeatherInfo(
                    main = "Clear",
                    description = "cielo despejado",
                    icon = "01d"
                )
            ),
            wind = null,
            visibility = 10000
        )
        
        coEvery { weatherRepository.getCurrentWeather(any()) } returns 
            kotlin.Result.success(expectedWeather)
        
        // When
        viewModel.loadWeather("Santiago,CL")
        
        // Then
        viewModel.uiState.test {
            val initialState = awaitItem()
            initialState.isLoading shouldBe false
            
            val loadingState = awaitItem()
            loadingState.isLoading shouldBe true
            
            val successState = awaitItem()
            successState.isLoading shouldBe false
            successState.weather shouldNotBe null
            successState.weather?.name shouldBe "Santiago"
            successState.weather?.main?.temp shouldBe 20.5
            successState.error shouldBe null
        }
    }
    
    @Test
    fun `should handle error when loading weather`() = runTest {
        // Given
        coEvery { weatherRepository.getCurrentWeather(any()) } returns 
            kotlin.Result.failure(Exception("Network error"))
        
        // When
        viewModel.loadWeather("Santiago,CL")
        
        // Then
        viewModel.uiState.test {
            skipItems(2) // Skip initial and loading states
            
            val errorState = awaitItem()
            errorState.isLoading shouldBe false
            errorState.weather shouldBe null
            errorState.error shouldBe "Network error"
        }
    }
    
    @Test
    fun `should refresh weather correctly`() = runTest {
        // Given
        val expectedWeather = WeatherApiResponse(
            name = "Santiago",
            main = WeatherMain(
                temp = 22.0,
                feels_like = 21.0,
                temp_min = 16.0,
                temp_max = 26.0,
                humidity = 65,
                pressure = 1015
            ),
            weather = listOf(
                WeatherInfo(
                    main = "Clouds",
                    description = "nubes dispersas",
                    icon = "02d"
                )
            ),
            wind = null,
            visibility = 10000
        )
        
        coEvery { weatherRepository.getCurrentWeather(any()) } returns 
            kotlin.Result.success(expectedWeather)
        
        // When
        viewModel.refreshWeather("Santiago,CL")
        
        // Then
        viewModel.uiState.test {
            skipItems(2) // Skip initial and loading states
            
            val successState = awaitItem()
            successState.weather shouldNotBe null
            successState.weather?.main?.temp shouldBe 22.0
        }
    }
}

