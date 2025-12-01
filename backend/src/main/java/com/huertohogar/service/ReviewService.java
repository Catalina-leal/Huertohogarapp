package com.huertohogar.service;

import com.huertohogar.model.dto.ReviewDTO;
import com.huertohogar.model.entity.Review;
import com.huertohogar.model.entity.User;
import com.huertohogar.repository.ReviewRepository;
import com.huertohogar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    
    public List<ReviewDTO> getReviewsByProduct(String productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public Double getAverageRating(String productId) {
        Double average = reviewRepository.getAverageRatingByProductId(productId);
        return average != null ? average : 0.0;
    }
    
    public ReviewDTO createReview(String productId, String userEmail, Integer rating, String comment) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Review review = new Review();
        review.setProductId(productId);
        review.setUserEmail(userEmail);
        review.setUserName(user.getFullName());
        review.setRating(rating);
        review.setComment(comment);
        
        Review saved = reviewRepository.save(review);
        return toDTO(saved);
    }
    
    public ReviewDTO updateReview(Long reviewId, String userEmail, Integer rating, String comment) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        
        if (!review.getUserEmail().equals(userEmail)) {
            throw new RuntimeException("No tienes permiso para modificar esta reseña");
        }
        
        review.setRating(rating);
        review.setComment(comment);
        
        Review updated = reviewRepository.save(review);
        return toDTO(updated);
    }
    
    public void deleteReview(Long reviewId, String userEmail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        
        if (!review.getUserEmail().equals(userEmail)) {
            throw new RuntimeException("No tienes permiso para eliminar esta reseña");
        }
        
        reviewRepository.delete(review);
    }
    
    private ReviewDTO toDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setProductId(review.getProductId());
        dto.setUserEmail(review.getUserEmail());
        dto.setUserName(review.getUserName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}
