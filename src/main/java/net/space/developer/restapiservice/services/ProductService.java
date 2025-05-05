package net.space.developer.restapiservice.services;

import net.space.developer.restapiservice.model.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Product service interface class to describe the services signature
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

public interface ProductService {

    /**
     * Get all the products int eh system without pagination
     *
     * @return @return a list of {@link ProductDTO}
     */
    List<ProductDTO> findAllProducts();

    /**
     * Get all the products in the system
     *
     * @param pageable a Page request with information
     * @return a list of {@link ProductDTO}
     */
    Page<ProductDTO> getAllProducts(Pageable pageable);

    /**
     * Get all the products by a given category
     *
     * @param category the product category
     * @param pageable a Page request with information
     * @return a list of {@link ProductDTO}
     */
    Page<ProductDTO> getProductsByCategory(String category, Pageable pageable);

    /**
     * Get all the products by a given name
     *
     * @param productName the product name
     * @param pageable a Page request with information
     * @return a list of {@link ProductDTO}
     */
    Page<ProductDTO> getProductsByName(String productName, Pageable pageable);

    /**
     * Get a product by a given identifier
     *
     * @param id the identifier of the product
     * @return the found instance of {@link ProductDTO}
     */
    ProductDTO getProductById(String id);

    /**
     * Create a new product from scratch
     *
     * @param product the product information
     * @return the new instance of {@link ProductDTO}
     */
    ProductDTO createProduct(ProductDTO product);

    /**
     * Update an exiting product by identifier
     *
     * @param id the product identifier
     * @param productDTO the updated information
     * @return an instance of {@link ProductDTO} updated
     */
    ProductDTO updateProduct(String id, ProductDTO productDTO);

    /**
     * Delete a given product by identifier
     *
     * @param id the product identifier
     * @return true if was deleted, false in other case
     */
    boolean deleteProduct(String id);
}
