package com.huertohogar.data.repository

import com.huertohogar.data.api.ExternalApiClient
import com.huertohogar.data.api.WeatherApiResponse
import com.huertohogar.data.api.WeatherApiService
import com.huertohogar.data.api.WeatherMain
import com.huertohogar.data.api.WeatherInfo
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class WeatherRepositoryTest {
    
    private lateinit var weatherApiService: WeatherApiService
    private lateinit var weatherRepository: WeatherRepository
    
    @BeforeEach
    fun setup() {
        weatherApiService = mockk()
        mockkObject(ExternalApiClient)
        weatherRepository = WeatherRepository()
    }
    
    @Test
    fun `should get current weather successfully`() = runTest {
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
        
        coEvery { ExternalApiClient.isWeatherApiAvailable() } returns true
        coEvery { ExternalApiClient.WEATHER_API_KEY } returns "test_key"
        coEvery { ExternalApiClient.weatherApiService.getCurrentWeather(any(), any()) } returns 
            Response.success(expectedWeather)
        
        // When
        val result = weatherRepository.getCurrentWeather("Santiago,CL")
        
        // Then
        result.isSuccess shouldBe true
        result.getOrNull() shouldNotBe null
        result.getOrNull()?.name shouldBe "Santiago"
        result.getOrNull()?.main?.temp shouldBe 20.5
    }
    
    @Test
    fun `should fail when API is not available`() = runTest {
        // Given
        coEvery { ExternalApiClient.isWeatherApiAvailable() } returns false
        
        // When
        val result = weatherRepository.getCurrentWeather("Santiago,CL")
        
        // Then
        result.isFailure shouldBe true
        result.exceptionOrNull()?.message shouldBe "API de clima no configurada"
    }
    
    @Test
    fun `should handle API error response`() = runTest {
        // Given
        coEvery { ExternalApiClient.isWeatherApiAvailable() } returns true
        coEvery { ExternalApiClient.WEATHER_API_KEY } returns "test_key"
        coEvery { 
            ExternalApiClient.weatherApiService.getCurrentWeather(any(), any()) 
        } returns Response.error(400, mockk())
        
        // When
        val result = weatherRepository.getCurrentWeather("Santiago,CL")
        
        // Then
        result.isFailure shouldBe true
    }
    
    @Test
    fun `should handle network exception`() = runTest {
        // Given
        coEvery { ExternalApiClient.isWeatherApiAvailable() } returns true
        coEvery { ExternalApiClient.WEATHER_API_KEY } returns "test_key"
        coEvery { 
            ExternalApiClient.weatherApiService.getCurrentWeather(any(), any()) 
        } throws Exception("Network error")
        
        // When
        val result = weatherRepository.getCurrentWeather("Santiago,CL")
        
        // Then
        result.isFailure shouldBe true
        result.exceptionOrNull()?.message shouldBe "Network error"
    }
}

