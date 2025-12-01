package com.huertohogar.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.huertohogar.presentation.Screen
import android.app.Application
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext

// aca con este código replicamos la funcionalidad de login.html
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.Factory(LocalContext.current.applicationContext as Application)
    )
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    Scaffold(topBar = { TopAppBar(title = { Text("Iniciar Sesión") }) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 24.dp))

            // Campo Correo
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Correo") }, placeholder = { Text("ejemplo@correo.com") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Campo Contraseña
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = "Mostrar contraseña")
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            val authState by viewModel.uiState.collectAsState()
            var showError by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf("") }

            LaunchedEffect(authState) {
                when (authState) {
                    is AuthUiState.Success -> {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                    is AuthUiState.Error -> {
                        errorMessage = (authState as AuthUiState.Error).message
                        showError = true
                        viewModel.resetUiState()
                    }
                    else -> Unit
                }
            }

            Button(
                onClick = { viewModel.login(email, password) },
                enabled = authState != AuthUiState.Loading && email.isNotBlank() && password.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (authState == AuthUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Iniciar Sesión")
                }
            }

            if (showError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Enlaces Olvido y Registro
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("¿Olvidaste tu contraseña?", modifier = Modifier.clickable { /* Navegar a olvido */ }, color = MaterialTheme.colorScheme.primary)
                Text("Registrarse", modifier = Modifier.clickable { navController.navigate(Screen.Register.route) }, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}