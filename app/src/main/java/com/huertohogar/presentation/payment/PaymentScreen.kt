package com.huertohogar.presentation.payment

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.huertohogar.presentation.AppBar
import com.huertohogar.presentation.Screen

enum class PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    BANK_TRANSFER,
    CASH_ON_DELIVERY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    orderId: String,
    totalAmount: Double,
    navController: NavHostController,
    viewModel: PaymentViewModel = viewModel(
        factory = PaymentViewModel.Factory(LocalContext.current.applicationContext as Application)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var selectedMethod by remember { mutableStateOf<PaymentMethod?>(null) }
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    LaunchedEffect(uiState.paymentSuccess) {
        if (uiState.paymentSuccess) {
            navController.navigate(Screen.OrderHistory.route) {
                popUpTo(Screen.Checkout.route) { inclusive = true }
            }
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
            // Resumen del pedido
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Resumen de Pago",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Pedido #${orderId.take(8)}")
                        Text(
                            "$${String.format("%.0f", totalAmount)} CLP",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Metodos de pago
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Método de Pago",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))

                    PaymentMethod.values().forEach { method ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = selectedMethod == method,
                                    onClick = { selectedMethod = method }
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedMethod == method,
                                onClick = { selectedMethod = method }
                            )
                            Spacer(Modifier.width(12.dp))
                            Icon(
                                when (method) {
                                    PaymentMethod.CREDIT_CARD -> Icons.Default.CreditCard
                                    PaymentMethod.DEBIT_CARD -> Icons.Default.AccountBalance
                                    PaymentMethod.BANK_TRANSFER -> Icons.Default.AccountBalanceWallet
                                    PaymentMethod.CASH_ON_DELIVERY -> Icons.Default.Money
                                },
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                when (method) {
                                    PaymentMethod.CREDIT_CARD -> "Tarjeta de Crédito"
                                    PaymentMethod.DEBIT_CARD -> "Tarjeta de Débito"
                                    PaymentMethod.BANK_TRANSFER -> "Transferencia Bancaria"
                                    PaymentMethod.CASH_ON_DELIVERY -> "Pago Contra Entrega"
                                },
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

            // Formulario de tarjeta en caso q seleccione tarjeta
            if (selectedMethod == PaymentMethod.CREDIT_CARD || selectedMethod == PaymentMethod.DEBIT_CARD) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Datos de la Tarjeta",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = { if (it.length <= 19) cardNumber = it },
                            label = { Text("Número de Tarjeta") },
                            placeholder = { Text("1234 5678 9012 3456") },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null) }
                        )
                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = cardHolder,
                            onValueChange = { cardHolder = it },
                            label = { Text("Titular de la Tarjeta") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = expiryDate,
                                onValueChange = { if (it.length <= 5) expiryDate = it },
                                label = { Text("MM/AA") },
                                placeholder = { Text("12/25") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = cvv,
                                onValueChange = { if (it.length <= 3) cvv = it },
                                label = { Text("CVV") },
                                placeholder = { Text("123") },
                                modifier = Modifier.weight(1f),
                                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
                            )
                        }
                    }
                }
            }

            // Informacion adicional segun el metodo
            if (selectedMethod == PaymentMethod.BANK_TRANSFER) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Datos para Transferencia",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Banco: Banco de Chile")
                        Text("Cuenta: 1234567890")
                        Text("RUT: 76.123.456-7")
                        Text("Nombre: HuertoHogar SpA")
                    }
                }
            }

            if (selectedMethod == PaymentMethod.CASH_ON_DELIVERY) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Pago Contra Entrega",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Puedes pagar en efectivo cuando recibas tu pedido. El repartidor traerá el cambio exacto.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Boton de pago
            Button(
                onClick = {
                    if (selectedMethod != null) {
                        viewModel.processPayment(
                            orderId = orderId,
                            amount = totalAmount,
                            paymentMethod = selectedMethod!!,
                            cardNumber = if (selectedMethod == PaymentMethod.CREDIT_CARD || selectedMethod == PaymentMethod.DEBIT_CARD) cardNumber else null,
                            cardHolder = if (selectedMethod == PaymentMethod.CREDIT_CARD || selectedMethod == PaymentMethod.DEBIT_CARD) cardHolder else null,
                            expiryDate = if (selectedMethod == PaymentMethod.CREDIT_CARD || selectedMethod == PaymentMethod.DEBIT_CARD) expiryDate else null,
                            cvv = if (selectedMethod == PaymentMethod.CREDIT_CARD || selectedMethod == PaymentMethod.DEBIT_CARD) cvv else null
                        )
                    }
                },
                enabled = !uiState.isProcessing && selectedMethod != null &&
                        (selectedMethod == PaymentMethod.CASH_ON_DELIVERY ||
                         selectedMethod == PaymentMethod.BANK_TRANSFER ||
                         (cardNumber.isNotBlank() && cardHolder.isNotBlank() && expiryDate.isNotBlank() && cvv.isNotBlank())),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp)
            ) {
                if (uiState.isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        "Pagar $${String.format("%.0f", totalAmount)} CLP",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
