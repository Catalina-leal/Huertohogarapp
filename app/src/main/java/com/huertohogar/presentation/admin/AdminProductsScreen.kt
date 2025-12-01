package com.huertohogar.presentation.admin

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.huertohogar.data.model.Product
import com.huertohogar.data.model.ProductCategory
import com.huertohogar.presentation.AppBar
import com.huertohogar.presentation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductsScreen(
    navController: NavHostController,
    viewModel: AdminProductsViewModel = viewModel(
        factory = AdminProductsViewModel.Factory(
            androidx.compose.ui.platform.LocalContext.current.applicationContext as Application
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<Product?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrar Productos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar producto")
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
                    items(uiState.products) { product ->
                        ProductAdminCard(
                            product = product,
                            onEdit = { editingProduct = it },
                            onDelete = { viewModel.deleteProduct(it.id) },
                            onToggleActive = { 
                                viewModel.updateProduct(
                                    product.copy(isActive = !product.isActive)
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    // Diálogo para agregar/editar productos
    if (showAddDialog || editingProduct != null) {
        ProductEditDialog(
            product = editingProduct,
            onDismiss = {
                showAddDialog = false
                editingProduct = null
            },
            onSave = { product ->
                if (editingProduct != null) {
                    viewModel.updateProduct(product)
                } else {
                    viewModel.addProduct(product)
                }
                showAddDialog = false
                editingProduct = null
            }
        )
    }
}

@Composable
fun ProductAdminCard(
    product: Product,
    onEdit: (Product) -> Unit,
    onDelete: (Product) -> Unit,
    onToggleActive: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (product.isActive) 
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
                        text = product.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = product.category.name.replace("_", " "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$${String.format("%.0f", product.price)} / ${product.unit}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (product.isActive) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            contentDescription = null,
                            tint = if (product.isActive) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (product.isActive) "Activo" else "Pausado",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (product.isActive) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.error
                        )
                    }
                }
                
                Row {
                    IconButton(onClick = onToggleActive) {
                        Icon(
                            imageVector = if (product.isActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (product.isActive) "Pausar" else "Activar",
                            tint = if (product.isActive) 
                                MaterialTheme.colorScheme.secondary 
                            else 
                                MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { onEdit(product) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { onDelete(product) }) {
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
fun ProductEditDialog(
    product: Product?,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var stock by remember { mutableStateOf(product?.stock?.toString() ?: "") }
    var category by remember { mutableStateOf(product?.category ?: ProductCategory.FRUTAS_FRESCAS) }
    var imageUrlName by remember { mutableStateOf(product?.imageUrlName ?: "") }
    var unit by remember { mutableStateOf(product?.unit ?: "kg") }
    var isActive by remember { mutableStateOf(product?.isActive ?: true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (product == null) "Agregar Producto" else "Editar Producto") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Precio") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = stock,
                        onValueChange = { stock = it },
                        label = { Text("Stock") },
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Unidad (kg, unidad, etc.)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = imageUrlName,
                    onValueChange = { imageUrlName = it },
                    label = { Text("Nombre de imagen (drawable)") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Selector de categoría
                Text("Categoría", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProductCategory.values().forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat.name.replace("_", " ")) }
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isActive,
                        onCheckedChange = { isActive = it }
                    )
                    Text("Producto activo")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    try {
                        val newProduct = Product(
                            id = product?.id ?: "PR${System.currentTimeMillis()}",
                            name = name,
                            description = description,
                            price = price.toDoubleOrNull() ?: 0.0,
                            stock = stock.toDoubleOrNull() ?: 0.0,
                            category = category,
                            imageUrlName = imageUrlName,
                            unit = unit,
                            isActive = isActive,
                            origin = product?.origin ?: "",
                            isOrganic = product?.isOrganic ?: false,
                            certifications = product?.certifications ?: "",
                            sustainablePractices = product?.sustainablePractices ?: "",
                            tag = product?.tag ?: "Frescos"
                        )
                        onSave(newProduct)
                    } catch (e: Exception) {
                        // Manejamos el error
                    }
                },
                enabled = name.isNotBlank() && price.toDoubleOrNull() != null && stock.toDoubleOrNull() != null
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
