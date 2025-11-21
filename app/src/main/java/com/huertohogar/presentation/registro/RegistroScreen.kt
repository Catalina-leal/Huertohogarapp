package com.huertohogar.presentation.registro

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.huertohogar.presentation.login.AuthUiState
import com.huertohogar.presentation.login.AuthViewModel
import com.huertohogar.presentation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    navController: NavHostController,
    // Inyectar AuthViewModel con su Factory
    viewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.Factory(LocalContext.current.applicationContext as Application)
    )
) {
    // manejo de estados de campos y UI
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState by viewModel.uiState.collectAsState()
    val showSnackbar = remember { mutableStateOf(false) }
    val snackbarMessage = remember { mutableStateOf("") }

    // Efecto para manejar el resultado del registro y la navegación
    LaunchedEffect(authState) {
        when (authState) {
            is AuthUiState.Success -> {
                snackbarMessage.value = "Registro exitoso. ¡Bienvenido!"
                showSnackbar.value = true
                // Navega a la Home y limpia la pila después del registro exitoso
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
            is AuthUiState.Error -> {
                snackbarMessage.value = (authState as AuthUiState.Error).message
                showSnackbar.value = true
                viewModel.resetUiState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Crear Cuenta") }) },
        snackbarHost = {
            SnackbarHost(hostState = SnackbarHostState()) { data ->
                Snackbar(data)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp)
                .verticalScroll(rememberScrollState()), // Permite scroll para móviles pequeños
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Registro de Usuario", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 24.dp))

            // Campo Nombre
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Nombre Completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Campo Correo
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Correo") },
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
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Campo Confirmar Contraseña
            OutlinedTextField(
                value = confirmPassword, onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(), // Siempre oculto para seguridad
                isError = password.isNotBlank() && confirmPassword.isNotBlank() && password != confirmPassword,
                supportingText = {
                    if (password.isNotBlank() && confirmPassword.isNotBlank() && password != confirmPassword) {
                        Text("Las contraseñas no coinciden.")
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            Button(
                onClick = {
                    if (password == confirmPassword) {
                        viewModel.register(name, email, password)
                    } else {
                        // Feedback al usuario (aunque ya está en supportingText)
                        snackbarMessage.value = "Las contraseñas deben coincidir."
                        showSnackbar.value = true
                    }
                },
                // Deshabilita si está cargando o las contraseñas no coinciden
                enabled = authState != AuthUiState.Loading && password == confirmPassword && password.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (authState == AuthUiState.Loading) {
                    CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Registrarse")
                }
            }

            Spacer(Modifier.height(16.dp))
            TextButton(onClick = { navController.popBackStack() }) {
                Text("¿Ya tienes cuenta? Inicia Sesión")
            }
        }
    }
}