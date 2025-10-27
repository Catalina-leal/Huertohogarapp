package com.huertohogar.huertohogarapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.huertohogar.huertohogarapp.model.Producto
import com.huertohogar.huertohogarapp.viewmodel.CartViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, cartViewModel: CartViewModel = viewModel()) {
    // Lista de productos usando el modelo Producto
    val productos = listOf(
        Producto(1, "Planta de albahaca", 10, 2500.0),
        Producto(2, "Macetero de barro", 5, 3500.0),
        Producto(3, "Tierra de hojas", 15, 2000.0)
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Huerto Hogar",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    // 🔹 Botón para ir al carrito
                    Button(onClick = { navController.navigate("cart") }) {
                        Text("Carrito")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // 🔹 Botón para ir al perfil
                    Button(onClick = { navController.navigate("profile") }) {
                        Text("Perfil")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(productos) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(product.nombre, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        Text("Precio: $${product.precio}", color = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            cartViewModel.addItem(product)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "${product.nombre} ha sido agregado con éxito",
                                    withDismissAction = true
                                )
                            }
                        }) {
                            Text("Agregar al carrito")
                        }
                    }
                }
            }
        }
    }
}


