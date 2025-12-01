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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
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
    val snackbarHostState = remember { SnackbarHostState() }

    // Efecto para manejar el resultado del registro y la navegación
    LaunchedEffect(authState) {
        when (authState) {
            is AuthUiState.Success -> {
                snackbarHostState.showSnackbar("Registro exitoso. ¡Bienvenido!")
                // Esperamos un momento antes de navegar para que el usuario vea el mensaje
                kotlinx.coroutines.delay(1000)
                // Navegamos a la Home y limpiamos la pila después del registro exitoso
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
            is AuthUiState.Error -> {
                snackbarHostState.showSnackbar((authState as AuthUiState.Error).message)
                viewModel.resetUiState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Crear Cuenta") }) },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
                    if (password == confirmPassword && name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                        viewModel.register(name, email, password)
                    } else {
                        // Mostramos feedback al usuario
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                            try {
                                snackbarHostState.showSnackbar("Por favor completa todos los campos correctamente")
                            } catch (e: Exception) {
                                android.util.Log.e("RegistroScreen", "Error mostrando snackbar", e)
                            }
                        }
                    }
                },
                // Habilitamos si todos los campos están completos y las contraseñas coinciden
                enabled = authState != AuthUiState.Loading 
                    && name.isNotBlank() 
                    && email.isNotBlank() 
                    && password.isNotBlank() 
                    && password.length >= 6
                    && password == confirmPassword,
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