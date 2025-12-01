package com.huertohogar.repository;

import com.huertohogar.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByProductIdOrderByCreatedAtDesc(String productId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.productId = :productId")
    Double getAverageRatingByProductId(@Param("productId") String productId);
    
    Optional<Review> findByProductIdAndUserEmail(String productId, String userEmail);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.productId = :productId")
    Long countByProductId(@Param("productId") String productId);
}
