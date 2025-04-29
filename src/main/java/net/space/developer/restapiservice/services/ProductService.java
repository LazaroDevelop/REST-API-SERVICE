package net.space.developer.restapiservice.services;

import net.space.developer.restapiservice.model.ProductDTO;

import java.util.List;

/**
 * Product service class to describe the services signature
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

public interface ProductService {
    List<ProductDTO> getAllProducts();
    List<ProductDTO> getProductsByCategory(String category);
    List<ProductDTO> getProductsByName(String productName);
    ProductDTO getProductById(int id);
    ProductDTO createProduct(ProductDTO product);
    ProductDTO updateProduct(int id, ProductDTO product);
    void deleteProduct(int id);
}
