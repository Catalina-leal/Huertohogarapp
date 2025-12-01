package com.huertohogar.presentation.admin

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.huertohogar.data.model.User
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(
    navController: NavHostController,
    viewModel: AdminUsersViewModel = viewModel(
        factory = AdminUsersViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingUser by remember { mutableStateOf<User?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrar Usuarios") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar usuario")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${uiState.error}")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.users) { user ->
                        UserAdminCard(
                            user = user,
                            onEdit = { editingUser = it },
                            onDelete = { viewModel.deleteUser(it.email) },
                            onToggleActive = {
                                viewModel.updateUser(
                                    user.copy(isActive = !user.isActive)
                                )
                            },
                            onToggleRole = {
                                val newRole = if (user.role == "ADMIN") "USER" else "ADMIN"
                                viewModel.updateUser(user.copy(role = newRole))
                            }
                        )
                    }
                }
            }
        }
    }

    // Diálogo para agregar/editar usuario
    if (showAddDialog || editingUser != null) {
        UserEditDialog(
            user = editingUser,
            onDismiss = {
                showAddDialog = false
                editingUser = null
            },
            onSave = { user ->
                if (editingUser != null) {
                    viewModel.updateUser(user)
                } else {
                    viewModel.addUser(user)
                }
                showAddDialog = false
                editingUser = null
            }
        )
    }
}

@Composable
fun UserAdminCard(
    user: User,
    onEdit: (User) -> Unit,
    onDelete: (User) -> Unit,
    onToggleActive: () -> Unit,
    onToggleRole: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (user.isActive) 
                MaterialTheme.colorScheme.surface 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.fullName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Badge(
                            containerColor = if (user.role == "ADMIN") 
                                MaterialTheme.colorScheme.secondary 
                            else 
                                MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(user.role, style = MaterialTheme.typography.labelSmall)
                        }
                        Icon(
                            imageVector = if (user.isActive) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            contentDescription = null,
                            tint = if (user.isActive) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (user.isActive) "Activo" else "Inactivo",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (user.isActive) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Puntos: ${user.loyaltyPoints}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                
                Row {
                    IconButton(onClick = onToggleRole) {
                        Icon(
                            Icons.Default.AdminPanelSettings,
                            contentDescription = if (user.role == "ADMIN") "Quitar admin" else "Hacer admin"
                        )
                    }
                    IconButton(onClick = onToggleActive) {
                        Icon(
                            imageVector = if (user.isActive) Icons.Default.Block else Icons.Default.Check,
                            contentDescription = if (user.isActive) "Desactivar" else "Activar"
                        )
                    }
                    IconButton(onClick = { onEdit(user) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { onDelete(user) }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserEditDialog(
    user: User?,
    onDismiss: () -> Unit,
    onSave: (User) -> Unit
) {
    var fullName by remember { mutableStateOf(user?.fullName ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var phone by remember { mutableStateOf(user?.phone ?: "") }
    var address by remember { mutableStateOf(user?.address ?: "") }
    var city by remember { mutableStateOf(user?.city ?: "") }
    var region by remember { mutableStateOf(user?.region ?: "") }
    var postalCode by remember { mutableStateOf(user?.postalCode ?: "") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(user?.role ?: "USER") }
    var isActive by remember { mutableStateOf(user?.isActive ?: true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (user == null) "Agregar Usuario" else "Editar Usuario") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = user == null // No permitimos cambiar email si es edición
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(if (user == null) "Contraseña" else "Nueva Contraseña (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )
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
                OutlinedTextField(
                    value = postalCode,
                    onValueChange = { postalCode = it },
                    label = { Text("Código Postal") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Selector de rol
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = role == "USER",
                        onClick = { role = "USER" }
                    )
                    Text("Usuario")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = role == "ADMIN",
                        onClick = { role = "ADMIN" }
                    )
                    Text("Administrador")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isActive,
                        onCheckedChange = { isActive = it }
                    )
                    Text("Usuario activo")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newUser = User(
                        email = email,
                        password = password.ifEmpty { user?.password ?: "" },
                        fullName = fullName,
                        phone = phone,
                        address = address,
                        city = city,
                        region = region,
                        postalCode = postalCode,
                        loyaltyPoints = user?.loyaltyPoints ?: 0,
                        role = role,
                        isActive = isActive,
                        createdAt = user?.createdAt ?: System.currentTimeMillis()
                    )
                    onSave(newUser)
                },
                enabled = fullName.isNotBlank() && email.isNotBlank() && (user != null || password.isNotBlank())
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
