package com.huertohogar.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private String orderItemId;
    private String productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}
