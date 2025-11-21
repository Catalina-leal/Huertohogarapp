package com.huertohogar.presentation.profile

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.huertohogar.presentation.AppBar
import com.huertohogar.presentation.Screen
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModel.Factory(LocalContext.current.applicationContext as Application)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val user by viewModel.currentUser.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var fullName by remember { mutableStateOf(user?.fullName ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var phone by remember { mutableStateOf(user?.phone ?: "") }
    var address by remember { mutableStateOf(user?.address ?: "") }
    var city by remember { mutableStateOf(user?.city ?: "") }
    var region by remember { mutableStateOf(user?.region ?: "") }
    var postalCode by remember { mutableStateOf(user?.postalCode ?: "") }

    LaunchedEffect(user) {
        user?.let {
            fullName = it.fullName
            email = it.email
            phone = it.phone
            address = it.address
            city = it.city
            region = it.region
            postalCode = it.postalCode
        }
    }

    Scaffold(
        topBar = { AppBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header con información del usuario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = user?.fullName ?: "Usuario",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user?.email ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(Modifier.height(8.dp))
                    
                    // Puntos de fidelización
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "${user?.loyaltyPoints ?: 0} puntos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            // Información personal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Información Personal",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { isEditing = !isEditing }) {
                            Icon(
                                if (isEditing) Icons.Default.Close else Icons.Default.Edit,
                                contentDescription = if (isEditing) "Cancelar" else "Editar"
                            )
                        }
                    }

                    if (isEditing) {
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Nombre Completo") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo Electrónico") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Teléfono") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { isEditing = false },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
                            }
                            Button(
                                onClick = {
                                    viewModel.updateUser(
                                        fullName = fullName,
                                        phone = phone,
                                        address = address,
                                        city = city,
                                        region = region,
                                        postalCode = postalCode
                                    )
                                    isEditing = false
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Guardar")
                            }
                        }
                    } else {
                        Spacer(Modifier.height(8.dp))
                        InfoRow("Nombre", user?.fullName ?: "")
                        InfoRow("Correo", user?.email ?: "")
                        InfoRow("Teléfono", (user?.phone ?: "").ifEmpty { "No especificado" })
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Dirección de envío
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Dirección de Envío",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))

                    if (isEditing) {
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Dirección") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = city,
                                onValueChange = { city = it },
                                label = { Text("Ciudad") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = region,
                                onValueChange = { region = it },
                                label = { Text("Región") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = postalCode,
                            onValueChange = { postalCode = it },
                            label = { Text("Código Postal") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        InfoRow("Dirección", (user?.address ?: "").ifEmpty { "No especificada" })
                        InfoRow("Ciudad", (user?.city ?: "").ifEmpty { "No especificada" })
                        InfoRow("Región", (user?.region ?: "").ifEmpty { "No especificada" })
                        if (!user?.postalCode.isNullOrEmpty()) {
                            InfoRow("Código Postal", user?.postalCode ?: "")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Panel de Admin (solo si el usuario es admin)
            if (user?.role == "ADMIN") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column {
                        ListItem(
                            headlineContent = { 
                                Text(
                                    "Panel de Administración",
                                    fontWeight = FontWeight.Bold
                                ) 
                            },
                            supportingContent = { 
                                Text("Gestionar productos, usuarios y ventas") 
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Default.AdminPanelSettings, 
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            },
                            trailingContent = {
                                Icon(Icons.Default.ChevronRight, contentDescription = null)
                            },
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.AdminDashboard.route)
                            }
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Opciones
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text("Historial de Pedidos") },
                        leadingContent = {
                            Icon(Icons.Default.History, contentDescription = null)
                        },
                        trailingContent = {
                            Icon(Icons.Default.ChevronRight, contentDescription = null)
                        },
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.OrderHistory.route)
                        }
                    )
                    Divider()
                    ListItem(
                        headlineContent = { Text("Cerrar Sesión") },
                        leadingContent = {
                            Icon(Icons.Default.Logout, contentDescription = null)
                        },
                        modifier = Modifier.clickable {
                            viewModel.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
