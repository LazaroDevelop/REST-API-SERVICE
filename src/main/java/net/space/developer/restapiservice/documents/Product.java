package net.space.developer.restapiservice.documents;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.space.developer.restapiservice.documents.enums.Category;
import net.space.developer.restapiservice.documents.enums.State;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Product document class to describe product properties
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("t_product")
public class Product {

    /**
     * Product entity id
     */
    @Id
    private String id;

    /**
     * Name field
     */
    @Field
    @NotNull
    private String name;

    /**
     * Description field
     */
    @Field
    private String description;

    /**
     * Category field
     */
    @Field
    @NotNull
    private Category category;

    /**
     * Price field
     */
    @Field
    @NotNull
    private Double price;

    /**
     * List of images related to the product
     */
    @Field
    private List<String> images;

    /**
     * Brand of the product
     */
    @Field
    private String brand;

    /**
     * Batch of the product
     */
    @Field
    @NotNull
    private String batch;

    /**
     * Product state
     */
    @Field
    @NotNull
    private State state;

    /**
     * Copy constructor
     *
     * @param product the product instance to copy
     */
    public Product(Product product){
        setId(product.getId());
        setName(product.getName());
        setDescription(product.getDescription());
        setCategory(product.getCategory());
        setPrice(product.getPrice());
        setImages(product.getImages());
        setBrand(product.getBrand());
        setBatch(product.getBatch());
        setState(product.getState());
    }
}
