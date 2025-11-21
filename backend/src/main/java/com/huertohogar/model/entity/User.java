package com.huertohogar.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    
    @Id
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;
    
    @Column(length = 20)
    private String phone;
    
    @Column(length = 500)
    private String address;
    
    @Column(length = 100)
    private String city;
    
    @Column(length = 100)
    private String region;
    
    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
