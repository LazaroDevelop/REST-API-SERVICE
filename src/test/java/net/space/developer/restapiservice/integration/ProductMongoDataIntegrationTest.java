package net.space.developer.restapiservice.integration;

import lombok.extern.slf4j.Slf4j;
import net.space.developer.restapiservice.config.TestcontainersConfiguration;
import net.space.developer.restapiservice.documents.Product;
import net.space.developer.restapiservice.documents.enums.Category;
import net.space.developer.restapiservice.documents.enums.State;
import net.space.developer.restapiservice.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Product MongoDB Data integration test
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

@Slf4j
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class ProductMongoDataIntegrationTest {

    /**
     * The current instance of the product in each test
     */
    private Product currentInstance;

    /**
     * Inject the product repository bean
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Truncate the repository before each operation
     */
    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    /**
     * Test create product feature
     */
    @Test
    @DisplayName("Should create a product")
    void shouldCreateProduct(){
        Product product = createProduct();
        var result = productRepository.save(product);

        this.currentInstance = result;

        assertEquals(result, product);
    }

    /**
     * Test find all products feature
     */
    @Test
    @DisplayName("Should find all the products")
    void shouldFindAllProducts(){
        List<Product> products = List.of(createProduct());
        var result = productRepository.saveAll(products);

        assertEquals(products, result);
        assertEquals(products.size(), result.size());
        assertEquals(products.getFirst(), result.getFirst());
        assertEquals(1, result.size());
        assertNotNull(result);
    }

    /**
     * Test find by id product feature
     */
    @Test
    @DisplayName("Should find a product by id")
    void shouldFindProductById(){
        Product product = createProduct();
        var result = productRepository.save(product);
        var foundProduct = productRepository.findById(result.getId());

        assertNotNull(foundProduct);
        foundProduct.ifPresent(value -> {
            this.currentInstance = value;
            assertEquals(product, value);
        });
    }

    /**
     * Test update product feature
     */
    @Test
    @DisplayName("Should update a product by id")
    void shouldUpdateProduct(){
        Product product = createProduct();

        Product fakeUpdate = new Product(product);
        fakeUpdate.setName("Product Update");

        var result = productRepository.save(product);
        var foundProduct = productRepository.findById(result.getId()).get();

        fakeUpdate.setId(foundProduct.getId());

        foundProduct.setName(fakeUpdate.getName());

        var updatedProduct = productRepository.save(foundProduct);

        this.currentInstance = updatedProduct;

        assertNotNull(updatedProduct);
        assertEquals(fakeUpdate, updatedProduct);
    }

    /**
     * Test delete product by id feature
     */
    @Test
    @DisplayName("Should delete a product")
    void shouldDeleteProduct(){
        Product product = createProduct();
        productRepository.save(product);

        var beforeDelete = productRepository.count();

        var result = productRepository.findById(product.getId()).get();

        productRepository.delete(result);

        var afterDelete = productRepository.count();

        log.info("Products before delete: {}", beforeDelete);
        log.info("Products after delete: {}", afterDelete);
        assertEquals(1, beforeDelete);
        assertEquals(0, afterDelete);
    }


    /**
     * Create an instance of {@link Product}
     *
     * @return a product with mock data information
     */
    private Product createProduct(){
        Product product = new Product();
        product.setName("Product1");
        product.setBrand("Product Brand");
        product.setDescription("Product Description");
        product.setState(State.IN_STOCK);
        product.setPrice(20.00D);
        product.setBatch("Product Batch");
        product.setImages(new ArrayList<>());
        product.setCategory(Category.CLOTHES);
        return product;
    }

    /**
     * After each execution print the current instance
     */
    @AfterEach
    void beforeComplete(){
        if(Objects.nonNull(this.currentInstance)){
            log.info("Current instance: {}", this.currentInstance);
        }
    }
}
