package net.space.developer.restapiservice.repository;

import net.space.developer.restapiservice.documents.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Product repository class
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    /**
     * Find all the products in the repository that match with the category
     *
     * @param category the given category
     * @param pageable the pageable information
     * @return an instance of {@link Page} with paged information
     */
    Page<Product> findByCategory(String category,Pageable pageable);

    /**
     * Find all the products in the repository that match with the name
     *
     * @param name the given name
     * @param pageable the pageable information
     * @return an instance of {@link Page} with paged information
     */
    Page<Product> findByNameContainingIgnoreCase(String name,Pageable pageable);
}
