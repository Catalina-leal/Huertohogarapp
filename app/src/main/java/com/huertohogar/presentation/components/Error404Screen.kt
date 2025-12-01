package com.huertohogar.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun Error404Screen(navController: NavHostController) {
    Scaffold { paddingValues -> //aca usamos Scaffold para el manejo el padding superior.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {

                Icon(
                    Icons.Default.Error,
                    contentDescription = "Error 404",
                    modifier = Modifier.size(96.dp),
                    tint = MaterialTheme.colorScheme.error
                )

                Spacer(Modifier.height(16.dp))

                Text("404", style = MaterialTheme.typography.displayLarge)
                Text("Página No Encontrada", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 24.dp))


                Text(
                    "Lo sentimos, la página que buscas no existe. Verifica la URL o vuelve a la página principal.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        // Regresa al inicio limpiando la pila de navegacion
                        navController.navigate(navController.graph.startDestinationId) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                ) {
                    Text("Volver al Inicio")
                }
            }
        }
    }
}