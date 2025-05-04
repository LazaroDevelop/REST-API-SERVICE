package net.space.developer.restapiservice.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.space.developer.restapiservice.documents.enums.Category;
import net.space.developer.restapiservice.documents.enums.State;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/**
 * Product data transfer object class to describe product properties
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO extends RepresentationModel<ProductDTO> {

    private String id;

    @NotNull(message = "Product name cannot be null")
    @NotBlank(message = "Product name is required")
    @Size(min = 5, max = 50)
    private String name;

    @Size(max = 255)
    private String description;

    @NotNull(message = "Product category cannot be null")
    @NotBlank(message = "Product category is required")
    private Category category;

    @Min(value = 0)
    private double price;
    private List<String> images;

    @Size(max = 50)
    private String brand;
    @Size(max = 50)
    private String batch;

    @NotNull(message = "Product state cannot be null")
    @NotBlank(message = "Product state is required")
    private State state;
}
