package com.huertohogar.service;

import com.huertohogar.model.dto.ProductDTO;
import com.huertohogar.model.entity.Product;
import com.huertohogar.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public ProductDTO getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        return toDTO(product);
    }
    
    public List<ProductDTO> getProductsByCategory(Product.ProductCategory category) {
        return productRepository.findByCategory(category).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductDTO> searchProducts(String query) {
        return productRepository.searchProducts(query).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductDTO> getOrganicProducts() {
        return productRepository.findByIsOrganicTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = toEntity(productDTO);
        Product saved = productRepository.save(product);
        return toDTO(saved);
    }
    
    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        
        existing.setName(productDTO.getName());
        existing.setDescription(productDTO.getDescription());
        existing.setPrice(productDTO.getPrice());
        existing.setOldPrice(productDTO.getOldPrice());
        existing.setStock(productDTO.getStock());
        existing.setCategory(productDTO.getCategory());
        existing.setImageUrl(productDTO.getImageUrl());
        existing.setTag(productDTO.getTag());
        existing.setOrigin(productDTO.getOrigin());
        existing.setUnit(productDTO.getUnit());
        existing.setIsOrganic(productDTO.getIsOrganic());
        existing.setCertifications(productDTO.getCertifications());
        existing.setSustainablePractices(productDTO.getSustainablePractices());
        
        Product updated = productRepository.save(existing);
        return toDTO(updated);
    }
    
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado: " + id);
        }
        productRepository.deleteById(id);
    }
    
    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setOldPrice(product.getOldPrice());
        dto.setStock(product.getStock());
        dto.setCategory(product.getCategory());
        dto.setImageUrl(product.getImageUrl());
        dto.setTag(product.getTag());
        dto.setOrigin(product.getOrigin());
        dto.setUnit(product.getUnit());
        dto.setIsOrganic(product.getIsOrganic());
        dto.setCertifications(product.getCertifications());
        dto.setSustainablePractices(product.getSustainablePractices());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
    
    private Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setOldPrice(dto.getOldPrice());
        product.setStock(dto.getStock());
        product.setCategory(dto.getCategory());
        product.setImageUrl(dto.getImageUrl());
        product.setTag(dto.getTag());
        product.setOrigin(dto.getOrigin());
        product.setUnit(dto.getUnit() != null ? dto.getUnit() : "kg");
        product.setIsOrganic(dto.getIsOrganic() != null ? dto.getIsOrganic() : false);
        product.setCertifications(dto.getCertifications());
        product.setSustainablePractices(dto.getSustainablePractices());
        return product;
    }
}
