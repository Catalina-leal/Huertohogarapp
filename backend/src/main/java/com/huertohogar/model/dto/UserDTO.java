package com.huertohogar.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String city;
    private String region;
    private Integer loyaltyPoints;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
