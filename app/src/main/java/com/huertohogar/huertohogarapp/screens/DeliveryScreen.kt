package com.huertohogar.huertohogarapp.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryScreen(navController: NavController) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var userLocation by remember { mutableStateOf<Location?>(null) }
    var direccionDespacho by remember { mutableStateOf("") }
    var tipoEntrega by remember { mutableStateOf("Despacho a domicilio") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 🔹 Lanza el permiso de ubicación cuando se necesite
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    userLocation = location
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar("No se pudo obtener tu ubicación.")
                    }
                }
            }
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Permiso de ubicación denegado.")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Opciones de entrega") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Selecciona tu tipo de entrega", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            // 🔹 Selección del tipo de entrega
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = tipoEntrega == "Despacho a domicilio",
                    onClick = { tipoEntrega = "Despacho a domicilio" }
                )
                Text("Despacho a domicilio")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = tipoEntrega == "Retiro en tienda",
                    onClick = { tipoEntrega = "Retiro en tienda" }
                )
                Text("Retiro en tienda")
            }

            if (tipoEntrega == "Despacho a domicilio") {
                OutlinedTextField(
                    value = direccionDespacho,
                    onValueChange = { direccionDespacho = it },
                    label = { Text("Dirección de despacho") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        when {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED -> {
                                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                    if (location != null) {
                                        userLocation = location
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("No se pudo obtener tu ubicación.")
                                        }
                                    }
                                }
                            }
                            else -> {
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }
                    }
                ) {
                    Text("Usar mi ubicación GPS")
                }

                userLocation?.let {
                    Text(
                        text = "Ubicación actual:\nLat: ${it.latitude}\nLon: ${it.longitude}",
                        fontSize = 14.sp
                    )
                }
            } else {
                Text(
                    "Puedes retirar tu pedido en:\nAv. Providencia 1234, Santiago",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Botón para confirmar pedido
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Compra finalizada con éxito. ¡Gracias por tu pedido!")
                    }
                    navController.navigate("home")
                }
            ) {
                Text("Confirmar pedido")
            }
        }
    }
}

