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
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, length = 200)
    private String name;
    
    @Column(length = 2000)
    private String description;
    
    @Column(nullable = false)
    private Double price;
    
    private Double oldPrice;
    
    @Column(nullable = false)
    private Double stock;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ProductCategory category;
    
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    @Column(length = 50)
    private String tag;
    
    @Column(length = 200)
    private String origin;
    
    @Column(length = 20)
    private String unit = "kg";
    
    @Column(name = "is_organic")
    private Boolean isOrganic = false;
    
    @Column(length = 500)
    private String certifications;
    
    @Column(name = "sustainable_practices", length = 500)
    private String sustainablePractices;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum ProductCategory {
        FRUTAS_FRESCAS,
        VERDURAS_ORGANICAS,
        PRODUCTOS_ORGANICOS,
        PRODUCTOS_LACTEOS
    }
}
