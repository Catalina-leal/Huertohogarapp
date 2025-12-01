package com.huertohogar.presentation.products

import android.app.Application
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.focusable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.huertohogar.data.model.ProductCategory
import com.huertohogar.presentation.AppBar
import com.huertohogar.presentation.Screen
import com.huertohogar.presentation.components.ProductCard
import com.huertohogar.presentation.cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    navController: NavHostController,
    viewModel: ProductsViewModel = viewModel(
        factory = ProductsViewModel.Factory(LocalContext.current.applicationContext as Application)
    ),
    cartViewModel: CartViewModel = viewModel(
        factory = CartViewModel.Factory(LocalContext.current.applicationContext as Application)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ProductCategory?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Ocultar teclado automáticamente cuando se encuentran resultados
    LaunchedEffect(uiState.products.size) {
        if (uiState.products.isNotEmpty() && searchQuery.isNotBlank() && !uiState.isLoading) {
            // Ocultar teclado después de un pequeño delay para que se muestren los resultados
            kotlinx.coroutines.delay(500)
            keyboardController?.hide()
        }
    }

    Scaffold(
        topBar = { AppBar(navController) },
        contentWindowInsets = WindowInsets(0.dp) // Eliminamos insets del Scaffold
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { newValue ->
                    searchQuery = newValue
                    // La búsqueda se aplica con debounce en el ViewModel
                    viewModel.searchProducts(newValue)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .onFocusChanged { focusState ->
                        // Si el campo pierde el foco y hay resultados, se ocultara el teclado
                        if (!focusState.isFocused && uiState.products.isNotEmpty()) {
                            keyboardController?.hide()
                        }
                    },
                placeholder = { Text("Buscar productos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                singleLine = true
            )

            // Filtros por categoría
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = {
                        selectedCategory = null
                        viewModel.filterByCategory(null)
                    },
                    label = { Text("Todos") }
                )
                ProductCategory.values().forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                            viewModel.filterByCategory(category)
                        },
                        label = { Text(category.name.replace("_", " ")) }
                    )
                }
            }

            // Lista de productos
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error: ${uiState.error}")
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(
                            start = 8.dp,
                            end = 8.dp,
                            top = 2.dp,
                            bottom = 8.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Función que se encarga de agregar un producto al carrito cuando hacemos click en agregar al carrito
                        items(uiState.products, key = { it.id }) { product ->
                            ProductCard(
                                product = product,
                                onAddToCart = { productToAdd ->
                                    cartViewModel.addToCart(
                                        productId = productToAdd.id,
                                        name = productToAdd.name,
                                        price = productToAdd.price,
                                        imageUrl = productToAdd.imageUrlName
                                    )
                                },
                                onViewDetails = { product ->
                                    navController.navigate(Screen.ProductDetail.createRoute(product.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
