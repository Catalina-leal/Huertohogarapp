package com.huertohogar.controller;

import com.huertohogar.model.dto.ApiResponse;
import com.huertohogar.model.dto.BlogPostDTO;
import com.huertohogar.service.BlogPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "API para gestión de posts del blog")
@CrossOrigin(origins = "*")
public class BlogController {
    
    private final BlogPostService blogPostService;
    
    @GetMapping("/posts")
    @Operation(summary = "Obtener todos los posts")
    public ResponseEntity<ApiResponse<List<BlogPostDTO>>> getAllPosts() {
        List<BlogPostDTO> posts = blogPostService.getAllPosts();
        return ResponseEntity.ok(ApiResponse.success(posts));
    }
    
    @GetMapping("/posts/{id}")
    @Operation(summary = "Obtener post por ID")
    public ResponseEntity<ApiResponse<BlogPostDTO>> getPostById(@PathVariable String id) {
        BlogPostDTO post = blogPostService.getPostById(id);
        return ResponseEntity.ok(ApiResponse.success(post));
    }
    
    @GetMapping("/posts/category/{category}")
    @Operation(summary = "Obtener posts por categoría")
    public ResponseEntity<ApiResponse<List<BlogPostDTO>>> getPostsByCategory(@PathVariable String category) {
        List<BlogPostDTO> posts = blogPostService.getPostsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }
    
    @PostMapping("/posts")
    @Operation(summary = "Crear nuevo post")
    public ResponseEntity<ApiResponse<BlogPostDTO>> createPost(@Valid @RequestBody BlogPostDTO postDTO) {
        BlogPostDTO created = blogPostService.createPost(postDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Post creado exitosamente", created));
    }
    
    @PutMapping("/posts/{id}")
    @Operation(summary = "Actualizar post")
    public ResponseEntity<ApiResponse<BlogPostDTO>> updatePost(
            @PathVariable String id,
            @Valid @RequestBody BlogPostDTO postDTO) {
        BlogPostDTO updated = blogPostService.updatePost(id, postDTO);
        return ResponseEntity.ok(ApiResponse.success("Post actualizado exitosamente", updated));
    }
    
    @DeleteMapping("/posts/{id}")
    @Operation(summary = "Eliminar post")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable String id) {
        blogPostService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success("Post eliminado exitosamente", null));
    }
}
