package com.huertohogar.service;

import com.huertohogar.model.dto.*;
import com.huertohogar.model.entity.Order;
import com.huertohogar.model.entity.OrderItem;
import com.huertohogar.model.entity.Product;
import com.huertohogar.model.entity.User;
import com.huertohogar.repository.OrderRepository;
import com.huertohogar.repository.ProductRepository;
import com.huertohogar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    
    public OrderDTO createOrder(String userEmail, CreateOrderRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));
        
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setShippingAddress(request.getShippingAddress());
        order.setCity(request.getCity());
        order.setRegion(request.getRegion());
        
        double totalAmount = 0.0;
        
        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemRequest.getProductId()));
            
            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para: " + product.getName());
            }
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setTotalPrice(product.getPrice() * itemRequest.getQuantity());
            
            totalAmount += orderItem.getTotalPrice();
            
            // Reducir stock
            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);
        }
        
        order.setTotalAmount(totalAmount);
        
        Order saved = orderRepository.save(order);
        return toDTO(saved);
    }
    
    public List<OrderDTO> getOrdersByUser(String userEmail) {
        return orderRepository.findByUserEmailOrderByOrderDateDesc(userEmail).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public OrderDTO getOrderById(String orderId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Order order = orderRepository.findByOrderIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + orderId));
        
        return toDTO(order);
    }
    
    public OrderDTO updateOrderStatus(String orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + orderId));
        
        order.setStatus(status);
        
        if (status == Order.OrderStatus.SHIPPED) {
            order.setTrackingNumber("TRACK-" + System.currentTimeMillis());
        }
        
        Order updated = orderRepository.save(order);
        return toDTO(updated);
    }
    
    private OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setUserEmail(order.getUser().getEmail());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setCity(order.getCity());
        dto.setRegion(order.getRegion());
        dto.setTrackingNumber(order.getTrackingNumber());
        dto.setCreatedAt(order.getCreatedAt());
        
        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(this::itemToDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    private OrderItemDTO itemToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setOrderItemId(item.getOrderItemId());
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setTotalPrice(item.getTotalPrice());
        return dto;
    }
}
