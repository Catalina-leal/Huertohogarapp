package com.huertohogar.presentation.payment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.huertohogar.data.api.ApiClient
import com.huertohogar.data.api.PaymentRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PaymentUiState(
    val isProcessing: Boolean = false,
    val paymentSuccess: Boolean = false,
    val error: String? = null,
    val transactionId: String? = null
)

class PaymentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()
// Función de procesamiento del pago
    fun processPayment(
        orderId: String,
        amount: Double,
        paymentMethod: PaymentMethod,
        cardNumber: String?,
        cardHolder: String?,
        expiryDate: String?,
        cvv: String?
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessing = true, error = null)

            try {
                // Simula el procesamiento de un pago
                delay(2000)

                // Si la API esta disponible, usara el backend real
                if (ApiClient.isApiAvailable()) {
                    val paymentRequest = PaymentRequest(
                        orderId = orderId,
                        amount = amount,
                        paymentMethod = when (paymentMethod) {
                            PaymentMethod.CREDIT_CARD -> "credit_card"
                            PaymentMethod.DEBIT_CARD -> "debit_card"
                            PaymentMethod.BANK_TRANSFER -> "transfer"
                            PaymentMethod.CASH_ON_DELIVERY -> "cash"
                        },
                        cardNumber = cardNumber,
                        cardHolder = cardHolder,
                        expiryDate = expiryDate,
                        cvv = cvv
                    )

                    val response = ApiClient.apiService.processPayment(paymentRequest)
                    if (response.isSuccessful && response.body() != null) {
                        val paymentResponse = response.body()!!
                        _uiState.value = _uiState.value.copy(
                            isProcessing = false,
                            paymentSuccess = paymentResponse.status == "success",
                            transactionId = paymentResponse.transactionId,
                            error = if (paymentResponse.status != "success") paymentResponse.message else null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isProcessing = false,
                            error = "Error al procesar el pago"
                        )
                    }
                } else {
                    // Simulacion  de pago en local
                    val success = when (paymentMethod) {
                        PaymentMethod.CASH_ON_DELIVERY -> true
                        PaymentMethod.BANK_TRANSFER -> true
                        PaymentMethod.CREDIT_CARD, PaymentMethod.DEBIT_CARD -> {
                            // Validación básica de tarjeta
                            cardNumber?.length == 19 && cvv?.length == 3
                        }
                    }

                    _uiState.value = _uiState.value.copy(
                        isProcessing = false,
                        paymentSuccess = success,
                        transactionId = if (success) "TXN-${System.currentTimeMillis()}" else null,
                        error = if (!success) "Datos de tarjeta inválidos" else null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    error = "Error al procesar el pago: ${e.message}"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = PaymentUiState()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PaymentViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
