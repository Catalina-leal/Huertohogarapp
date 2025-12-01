package com.huertohogar.data.repository

import com.huertohogar.data.api.ExternalApiClient
import com.huertohogar.data.api.WeatherApiResponse
import com.huertohogar.data.api.WeatherForecastApiResponse

/**
 * Repositorio para manejar datos de la API externa de clima
 * Este repositorio se encarga de obtener información del clima
 * que puede afectar a los productos agrícolas
 */
class WeatherRepository(
    private val weatherApiService: com.huertohogar.data.api.WeatherApiService = ExternalApiClient.weatherApiService
) {
    
    /**
     * Obtenemos el clima actual de una ciudad
     */
    suspend fun getCurrentWeather(city: String = "Santiago,CL"): Result<WeatherApiResponse> {
        return try {
            if (!ExternalApiClient.isWeatherApiAvailable()) {
                return Result.failure(Exception("API de clima no configurada"))
            }
            
            val response = weatherApiService.getCurrentWeather(
                city = city,
                appid = ExternalApiClient.WEATHER_API_KEY
            )
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "API key inválida o no autorizada. Verifica tu API key de OpenWeatherMap en local.properties y reinicia la app."
                    404 -> "Ciudad no encontrada. Verifica el nombre de la ciudad."
                    else -> {
                        "Error al obtener clima: ${response.message()} (Código: ${response.code()})"
                    }
                }
                android.util.Log.e("WeatherRepository", "Error al obtener clima: ${response.code()} - $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtenemos el pronóstico del clima
     */
    suspend fun getWeatherForecast(city: String = "Santiago,CL"): Result<WeatherForecastApiResponse> {
        return try {
            if (!ExternalApiClient.isWeatherApiAvailable()) {
                return Result.failure(Exception("API de clima no configurada"))
            }
            
            val response = weatherApiService.getWeatherForecast(
                city = city,
                appid = ExternalApiClient.WEATHER_API_KEY
            )
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "API key inválida o no autorizada. Verifica tu API key de OpenWeatherMap en local.properties y reinicia la app."
                    404 -> "Ciudad no encontrada. Verifica el nombre de la ciudad."
                    else -> {
                        "Error al obtener pronóstico: ${response.message()} (Código: ${response.code()})"
                    }
                }
                android.util.Log.e("WeatherRepository", "Error al obtener pronóstico: ${response.code()} - $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

