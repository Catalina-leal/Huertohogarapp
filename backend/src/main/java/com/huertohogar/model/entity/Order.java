package com.huertohogar.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", nullable = false)
    private User user;
    
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private OrderStatus status;
    
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;
    
    @Column(name = "shipping_address", length = 500)
    private String shippingAddress;
    
    @Column(length = 100)
    private String city;
    
    @Column(length = 100)
    private String region;
    
    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        PREPARING,
        SHIPPED,
        IN_TRANSIT,
        DELIVERED,
        CANCELLED
    }
}
