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
@Table(name = "blog_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BlogPost {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(length = 10000)
    private String content;
    
    @Column(nullable = false, length = 200)
    private String author;
    
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    @Column(name = "published_date", nullable = false)
    private LocalDateTime publishedDate;
    
    @Column(length = 100)
    private String category;
    
    @Column(name = "read_time")
    private Integer readTime;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
