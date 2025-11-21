package com.huertohogar.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostDTO {
    private String id;
    private String title;
    private String content;
    private String author;
    private String imageUrl;
    private LocalDateTime publishedDate;
    private String category;
    private Integer readTime;
    private LocalDateTime createdAt;
}
