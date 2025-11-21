package com.huertohogar.controller;

import com.huertohogar.model.dto.ApiResponse;
import com.huertohogar.model.dto.ProductDTO;
import com.huertohogar.model.entity.Product;
import com.huertohogar.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "API para gestión de productos")
@CrossOrigin(origins = "*")
public class ProductController {
    
    private final ProductService productService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los productos")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable String id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }
    
    @GetMapping("/category/{category}")
    @Operation(summary = "Obtener productos por categoría")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByCategory(
            @PathVariable Product.ProductCategory category) {
        List<ProductDTO> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Buscar productos")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> searchProducts(
            @RequestParam String query) {
        List<ProductDTO> products = productService.searchProducts(query);
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @GetMapping("/organic")
    @Operation(summary = "Obtener productos orgánicos")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getOrganicProducts() {
        List<ProductDTO> products = productService.getOrganicProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo producto")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO created = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Producto creado exitosamente", created));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updated = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(ApiResponse.success("Producto actualizado exitosamente", updated));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Producto eliminado exitosamente", null));
    }
}
