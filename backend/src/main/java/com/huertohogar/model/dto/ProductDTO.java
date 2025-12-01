package com.huertohogar.model.dto;

import com.huertohogar.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String id;
    private String name;
    private String description;
    private Double price;
    private Double oldPrice;
    private Double stock;
    private Product.ProductCategory category;
    private String imageUrl;
    private String tag;
    private String origin;
    private String unit;
    private Boolean isOrganic;
    private String certifications;
    private String sustainablePractices;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
