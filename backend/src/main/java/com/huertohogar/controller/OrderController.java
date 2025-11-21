package com.huertohogar.controller;

import com.huertohogar.model.dto.ApiResponse;
import com.huertohogar.model.dto.CreateOrderRequest;
import com.huertohogar.model.dto.OrderDTO;
import com.huertohogar.model.entity.Order;
import com.huertohogar.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "API para gestión de pedidos")
@CrossOrigin(origins = "*")
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    @Operation(summary = "Crear nuevo pedido")
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(
            @RequestHeader("X-User-Email") String userEmail,
            @Valid @RequestBody CreateOrderRequest request) {
        OrderDTO order = orderService.createOrder(userEmail, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pedido creado exitosamente", order));
    }
    
    @GetMapping("/user/{email}")
    @Operation(summary = "Obtener pedidos de un usuario")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersByUser(@PathVariable String email) {
        List<OrderDTO> orders = orderService.getOrdersByUser(email);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }
    
    @GetMapping("/{orderId}")
    @Operation(summary = "Obtener pedido por ID")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderById(
            @PathVariable String orderId,
            @RequestHeader("X-User-Email") String userEmail) {
        OrderDTO order = orderService.getOrderById(orderId, userEmail);
        return ResponseEntity.ok(ApiResponse.success(order));
    }
    
    @PutMapping("/{orderId}/status")
    @Operation(summary = "Actualizar estado del pedido")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody Order.OrderStatus status) {
        OrderDTO order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(ApiResponse.success("Estado actualizado exitosamente", order));
    }
}
