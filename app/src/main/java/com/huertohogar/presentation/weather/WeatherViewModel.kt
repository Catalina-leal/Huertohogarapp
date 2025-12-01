package com.huertohogar.presentation.weather

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.api.WeatherApiResponse
import com.huertohogar.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI para la información del clima
 */
data class WeatherUiState(
    val isLoading: Boolean = false,
    val weather: WeatherApiResponse? = null,
    val error: String? = null
)

/**
 * ViewModel para manejar la lógica del clima
 * Utiliza la API externa de OpenWeatherMap
 */
class WeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    
    /**
     * Cargamos el clima actual
     */
    fun loadWeather(city: String = "Santiago,CL") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            weatherRepository.getCurrentWeather(city)
                .onSuccess { weather ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weather = weather,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weather = null,
                        error = exception.message ?: "Error desconocido"
                    )
                }
        }
    }
    
    /**
     * Refrescamos el clima
     */
    fun refreshWeather(city: String = "Santiago,CL") {
        loadWeather(city)
    }
    
    /**
     * Factory para instanciar el ViewModel
     */
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                val weatherRepository = WeatherRepository()
                
                @Suppress("UNCHECKED_CAST")
                return WeatherViewModel(weatherRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

