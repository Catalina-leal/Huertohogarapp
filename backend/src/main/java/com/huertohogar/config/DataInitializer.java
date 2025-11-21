package com.huertohogar.config;

import com.huertohogar.model.entity.Product;
import com.huertohogar.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final ProductRepository productRepository;
    
    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            List<Product> initialProducts = Arrays.asList(
                // Frutas Frescas
                createProduct("FR001", "Manzanas Fuji", 
                    "Manzanas Fuji crujientes y dulces, cultivadas en el Valle del Maule.",
                    1200.0, null, 150.0, Product.ProductCategory.FRUTAS_FRESCAS, "product_1", "Frescos", "Valle del Maule", "kg", false),
                
                createProduct("FR002", "Naranjas Valencia",
                    "Jugosas y ricas en vitamina C, estas naranjas Valencia son ideales para zumos frescos.",
                    1000.0, null, 200.0, Product.ProductCategory.FRUTAS_FRESCAS, "naranjas", "Frescos", "Región de Valparaíso", "kg", false),
                
                createProduct("FR003", "Plátanos Cavendish",
                    "Plátanos maduros y dulces, perfectos para el desayuno o como snack energético.",
                    800.0, null, 250.0, Product.ProductCategory.FRUTAS_FRESCAS, "bananas", "Frescos", "Región de O'Higgins", "kg", false),
                
                // Verduras Orgánicas
                createProduct("VR001", "Zanahorias Orgánicas",
                    "Zanahorias crujientes cultivadas sin pesticidas en la Región de O'Higgins.",
                    900.0, null, 100.0, Product.ProductCategory.VERDURAS_ORGANICAS, "zanahorias_1024x683", "Orgánicas", "Región de O'Higgins", "kg", true, "Certificación Orgánica", "Cultivo sin pesticidas, rotación de cultivos"),
                
                createProduct("VR002", "Espinacas Frescas",
                    "Espinacas frescas y nutritivas, perfectas para ensaladas y batidos verdes.",
                    700.0, null, 80.0, Product.ProductCategory.VERDURAS_ORGANICAS, "espinaca", "Orgánicas", "Región Metropolitana", "bolsa", true, "Certificación Orgánica", ""),
                
                createProduct("VR003", "Pimientos Tricolores",
                    "Pimientos rojos, amarillos y verdes, ideales para salteados y platos coloridos.",
                    1500.0, null, 120.0, Product.ProductCategory.VERDURAS_ORGANICAS, "pimiento_tricolor", "Frescos", "Región de Valparaíso", "kg", false),
                
                // Productos Orgánicos
                createProduct("PO001", "Miel Orgánica",
                    "Miel pura y orgánica producida por apicultores locales.",
                    5000.0, null, 50.0, Product.ProductCategory.PRODUCTOS_ORGANICOS, "product_1", "Orgánico", "Región de Los Lagos", "frasco", true, "Certificación Orgánica", "Apicultura sostenible, sin químicos"),
                
                createProduct("PO003", "Quinua Orgánica",
                    "Quinua orgánica de alta calidad, rica en proteínas y nutrientes esenciales.",
                    3500.0, null, 75.0, Product.ProductCategory.PRODUCTOS_ORGANICOS, "product_1", "Orgánico", "Región de Tarapacá", "kg", true, "Certificación Orgánica", ""),
                
                // Productos Lácteos
                createProduct("PL001", "Leche Entera",
                    "Leche entera fresca proveniente de granjas locales.",
                    1000.0, null, 200.0, Product.ProductCategory.PRODUCTOS_LACTEOS, "leche", "Fresco", "Región de Los Lagos", "lt", false)
            );
            
            productRepository.saveAll(initialProducts);
            System.out.println("✅ Productos iniciales cargados: " + initialProducts.size());
        }
    }
    
    private Product createProduct(String id, String name, String description, Double price, 
                                  Double oldPrice, Double stock, Product.ProductCategory category,
                                  String imageUrl, String tag, String origin, String unit, Boolean isOrganic) {
        return createProduct(id, name, description, price, oldPrice, stock, category, imageUrl, tag, origin, unit, isOrganic, "", "");
    }
    
    private Product createProduct(String id, String name, String description, Double price,
                                  Double oldPrice, Double stock, Product.ProductCategory category,
                                  String imageUrl, String tag, String origin, String unit, 
                                  Boolean isOrganic, String certifications, String sustainablePractices) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setOldPrice(oldPrice);
        product.setStock(stock);
        product.setCategory(category);
        product.setImageUrl(imageUrl);
        product.setTag(tag);
        product.setOrigin(origin);
        product.setUnit(unit);
        product.setIsOrganic(isOrganic);
        product.setCertifications(certifications);
        product.setSustainablePractices(sustainablePractices);
        return product;
    }
}
