package com.huertohogar.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreLocation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, length = 200)
    private String name;
    
    @Column(nullable = false, length = 500)
    private String address;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @Column(length = 20)
    private String phone;
    
    @Column(length = 200)
    private String email;
    
    @Column(name = "opening_hours", length = 200)
    private String openingHours;
}
