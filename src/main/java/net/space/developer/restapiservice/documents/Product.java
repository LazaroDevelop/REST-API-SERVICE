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

    @Id
    private String id;

    @Field
    @NotNull
    private String name;

    @Field
    private String description;

    @Field
    @NotNull
    private Category category;

    @Field
    @NotNull
    private Double price;

    @Field
    private List<String> images;

    @Field
    private String brand;

    @Field
    @NotNull
    private String batch;

    @Field
    @NotNull
    private State state;

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
