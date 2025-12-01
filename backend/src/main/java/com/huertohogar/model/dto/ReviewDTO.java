package com.huertohogar.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private String productId;
    private String userEmail;
    private String userName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
