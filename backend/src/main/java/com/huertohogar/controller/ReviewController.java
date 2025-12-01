package com.huertohogar.controller;

import com.huertohogar.model.dto.ApiResponse;
import com.huertohogar.model.dto.ReviewDTO;
import com.huertohogar.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "Reseñas", description = "API para gestión de reseñas")
@CrossOrigin(origins = "*")
public class ReviewController {
    
    private final ReviewService reviewService;
    
    @GetMapping("/product/{productId}")
    @Operation(summary = "Obtener reseñas de un producto")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByProduct(@PathVariable String productId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }
    
    @GetMapping("/product/{productId}/average")
    @Operation(summary = "Obtener calificación promedio de un producto")
    public ResponseEntity<ApiResponse<Double>> getAverageRating(@PathVariable String productId) {
        Double average = reviewService.getAverageRating(productId);
        return ResponseEntity.ok(ApiResponse.success(average));
    }
    
    @PostMapping
    @Operation(summary = "Crear nueva reseña")
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestParam String productId,
            @RequestParam Integer rating,
            @RequestParam(required = false) String comment) {
        ReviewDTO review = reviewService.createReview(productId, userEmail, rating, comment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reseña creada exitosamente", review));
    }
    
    @PutMapping("/{reviewId}")
    @Operation(summary = "Actualizar reseña")
    public ResponseEntity<ApiResponse<ReviewDTO>> updateReview(
            @PathVariable Long reviewId,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestParam Integer rating,
            @RequestParam(required = false) String comment) {
        ReviewDTO review = reviewService.updateReview(reviewId, userEmail, rating, comment);
        return ResponseEntity.ok(ApiResponse.success("Reseña actualizada exitosamente", review));
    }
    
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Eliminar reseña")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("X-User-Email") String userEmail) {
        reviewService.deleteReview(reviewId, userEmail);
        return ResponseEntity.ok(ApiResponse.success("Reseña eliminada exitosamente", null));
    }
}
