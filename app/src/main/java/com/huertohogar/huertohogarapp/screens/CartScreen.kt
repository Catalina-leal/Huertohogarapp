package com.huertohogar.huertohogarapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun CartScreen(navController: NavController, cartViewModel: CartViewModel = viewModel()) {
    val items = cartViewModel.items
    val neto = cartViewModel.neto.value
    val iva = cartViewModel.iva.value
    val total = cartViewModel.total.value

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (items.isEmpty()) {
                Text("Tu carrito está vacío.", fontSize = 18.sp)
                Button(onClick = { navController.navigate("home") }) {
                    Text("Volver al inicio")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items) { producto ->
                        CartItemRow(producto, cartViewModel)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar cálculos detallados
                Text("Subtotal: $${"%.0f".format(neto)}", fontSize = 16.sp)
                Text("IVA (19%): $${"%.0f".format(iva)}", fontSize = 16.sp)
                Text("Total: $${"%.0f".format(total)}", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { navController.navigate("home") }) {
                        Text("Volver al inicio")
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        onClick = {
                            navController.navigate("delivery")
                            scope.launch {
                                snackbarHostState.showSnackbar(" Compra finalizada con éxito")
                            }
                        }
                    ) {
                        Text("Finalizar compra")
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(producto: Producto, cartViewModel: CartViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(producto.nombre, fontSize = 18.sp)
                Text("x${producto.cantidad}  -  $${producto.precio * producto.cantidad}")
            }
            Button(onClick = { cartViewModel.removeItem(producto) }) {
                Text("Eliminar")
            }
        }
    }
}



