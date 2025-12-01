package com.huertohogar.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * nuestro cliente Retrofit para la comunicacion con el API
 * 
 *
 */
object ApiClient {
    // la URL de nuestro backend Spring Boot
    // ejemplo para el desarrollo local: seria este  "http://10.0.2.2:8080/api/v1/" (Android Emulator)
    // y para dispositivo fisico:  seria "http://MI_IP_LOCAL:8080/api/v1/"

    private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val apiService: ApiService = retrofit.create(ApiService::class.java)
    

    fun isApiAvailable(): Boolean {

        return true
    }
}
