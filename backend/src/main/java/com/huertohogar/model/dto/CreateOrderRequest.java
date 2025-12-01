package com.huertohogar.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    
    @NotEmpty(message = "El pedido debe tener al menos un producto")
    @Valid
    private List<OrderItemRequest> items;
    
    @NotBlank(message = "La dirección de envío es requerida")
    private String shippingAddress;
    
    @NotBlank(message = "La ciudad es requerida")
    private String city;
    
    @NotBlank(message = "La región es requerida")
    private String region;
}
