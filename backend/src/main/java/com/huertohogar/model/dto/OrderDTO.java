package com.huertohogar.model.dto;

import com.huertohogar.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String orderId;
    private String userEmail;
    private LocalDateTime orderDate;
    private Order.OrderStatus status;
    private Double totalAmount;
    private String shippingAddress;
    private String city;
    private String region;
    private String trackingNumber;
    private List<OrderItemDTO> items;
    private LocalDateTime createdAt;
}
