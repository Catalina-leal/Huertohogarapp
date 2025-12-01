package com.huertohogar.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Servicio para la API externa de clima (OpenWeatherMap)
 * Utilizamos esta API para mostrar información del clima que afecta a los productos agrícolas
 */
interface WeatherApiService {
    
    /**
     * Obtenemos el clima actual por ciudad
     * @param city Nombre de la ciudad
     * @param appid API Key de OpenWeatherMap
     * @param units Unidades de temperatura (metric para Celsius)
     * @param lang Idioma de la respuesta (es para español)
     */
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): Response<WeatherApiResponse>
    
    /**
     * Obtenemos el pronóstico del clima
     */
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es",
        @Query("cnt") count: Int = 5
    ): Response<WeatherForecastApiResponse>
}

/**
 * Respuesta de la API de clima actual
 */
data class WeatherApiResponse(
    val name: String,
    val main: WeatherMain,
    val weather: List<WeatherInfo>,
    val wind: WeatherWind?,
    val visibility: Int?
)

data class WeatherMain(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int,
    val pressure: Int
)

data class WeatherInfo(
    val main: String,
    val description: String,
    val icon: String
)

data class WeatherWind(
    val speed: Double,
    val deg: Int?
)

/**
 * Respuesta del pronóstico del clima
 */
data class WeatherForecastApiResponse(
    val list: List<WeatherForecastItem>,
    val city: WeatherCity
)

data class WeatherForecastItem(
    val dt: Long,
    val main: WeatherMain,
    val weather: List<WeatherInfo>,
    val dt_txt: String
)

data class WeatherCity(
    val name: String,
    val country: String
)

