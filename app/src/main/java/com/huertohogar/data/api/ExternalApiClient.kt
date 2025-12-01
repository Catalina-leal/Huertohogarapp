package com.huertohogar.data.api

import com.huertohogar.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit para APIs externas
 * Utilizamos OpenWeatherMap para obtener información del clima
 * Esto no interfiere con nuestros microservicios ni datos locales
 */
object ExternalApiClient {
    
    // URL base de OpenWeatherMap API
    private const val WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"
    
    // Obtenemos la API Key desde BuildConfig (configurada en build.gradle.kts)
    // La API key se lee desde local.properties y se inyecta durante la compilación
    val WEATHER_API_KEY: String = BuildConfig.WEATHER_API_KEY
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val weatherRetrofit = Retrofit.Builder()
        .baseUrl(WEATHER_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val weatherApiService: WeatherApiService = weatherRetrofit.create(WeatherApiService::class.java)
    
    /**
     * Verificamos si la API externa está disponible
     * Retornamos false si no hay API key configurada
     */
    fun isWeatherApiAvailable(): Boolean {
        val isAvailable = WEATHER_API_KEY.isNotEmpty() && 
                         WEATHER_API_KEY != "YOUR_WEATHER_API_KEY_HERE" &&
                         WEATHER_API_KEY != ""
        if (!isAvailable) {
            android.util.Log.w("ExternalApiClient", "API key de clima no configurada o vacía")
        } else {
            android.util.Log.d("ExternalApiClient", "API key de clima configurada: ${WEATHER_API_KEY.take(8)}...")
        }
        return isAvailable
    }
}

