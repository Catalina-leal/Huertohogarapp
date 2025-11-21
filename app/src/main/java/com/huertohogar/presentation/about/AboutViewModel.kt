package com.huertohogar.presentation.about

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.db.AppDatabase
import com.huertohogar.data.model.StoreLocation
import com.huertohogar.data.repository.StoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AboutUiState(
    val storeLocations: List<StoreLocation> = emptyList(),
    val isLoading: Boolean = false
)

class AboutViewModel(
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutUiState(isLoading = true))
    val uiState: StateFlow<AboutUiState> = _uiState.asStateFlow()

    init {
        loadStoreLocations()
    }

    private fun loadStoreLocations() {
        viewModelScope.launch {
            storeRepository.getAllStoreLocations().collect { locations ->
                _uiState.value = _uiState.value.copy(
                    storeLocations = locations,
                    isLoading = false
                )
            }
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AboutViewModel::class.java)) {
                val database = AppDatabase.getDatabase(application)
                val storeLocationDao = database.storeLocationDao()
                val storeRepository = StoreRepository(storeLocationDao)

                @Suppress("UNCHECKED_CAST")
                return AboutViewModel(storeRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
