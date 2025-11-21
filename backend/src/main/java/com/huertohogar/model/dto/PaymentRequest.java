package com.huertohogar.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    @NotBlank(message = "El ID del pedido es requerido")
    private String orderId;
    
    @NotNull(message = "El monto es requerido")
    @Positive(message = "El monto debe ser positivo")
    private Double amount;
    
    @NotBlank(message = "El método de pago es requerido")
    private String paymentMethod; // "credit_card", "debit_card", "transfer", "cash"
    
    private String cardNumber;
    private String cardHolder;
    private String expiryDate;
    private String cvv;
}
