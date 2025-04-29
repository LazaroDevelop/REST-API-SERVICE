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
    Page<Product> findByCategory(String category,Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name,Pageable pageable);
}
