package com.huertohogar.presentation.products

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.db.AppDatabase
import com.huertohogar.data.model.Product
import com.huertohogar.data.model.ProductCategory
import com.huertohogar.data.repository.ProductRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

data class ProductsUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@OptIn(FlowPreview::class)
class ProductsViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState(isLoading = true))
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    private var allProducts = listOf<Product>()
    private var currentSearchQuery: String = ""
    private var currentCategory: ProductCategory? = null
    
    // Debounce para búsqueda: esperamos 300ms antes de aplicar filtros
    private val searchQueryFlow = MutableStateFlow("")

    init {
        loadProducts()
        // Observamos cambios en la búsqueda con debounce
        viewModelScope.launch {
            searchQueryFlow
                .debounce(300) // Espera 300ms después del último cambio
                .collect { query ->
                    currentSearchQuery = query
                    applyFiltersInternal()
                }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                productRepository.getAllProducts().collect { products ->
                    allProducts = products
                    // Aplicamos filtros directamente sin lanzar otra corrutina
                    applyFiltersInternal()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    private fun applyFiltersInternal() {
        var filtered = allProducts
        
        // Aplicamos filtro de categoría
        if (currentCategory != null) {
            filtered = filtered.filter { it.category == currentCategory }
        }
        
        // Aplicamos búsqueda
        if (currentSearchQuery.isNotBlank()) {
            val searchLower = currentSearchQuery.lowercase()
            filtered = filtered.filter {
                it.name.lowercase().contains(searchLower) ||
                it.description.lowercase().contains(searchLower)
            }
        }
        
        _uiState.value = _uiState.value.copy(
            products = filtered,
            isLoading = false
        )
    }
    
    private fun applyFilters() {
        // Aplicamos filtros sin lanzar corrutina adicional
        applyFiltersInternal()
    }

    fun filterByCategory(category: ProductCategory?) {
        currentCategory = category
        applyFilters()
    }

    fun searchProducts(query: String) {
        // Actualizamos el flujo de búsqueda (debounce aplicará los filtros automáticamente)
        searchQueryFlow.value = query
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductsViewModel::class.java)) {
                val database = AppDatabase.getDatabase(application)
                val productDao = database.productDao()
                val repository = ProductRepository(productDao)
                @Suppress("UNCHECKED_CAST")
                return ProductsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
