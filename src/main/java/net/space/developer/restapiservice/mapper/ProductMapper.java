package net.space.developer.restapiservice.mapper;

import net.space.developer.restapiservice.documents.Product;
import net.space.developer.restapiservice.model.ProductDTO;
import org.mapstruct.Mapper;

/**
 * Product mapper interface to map from DTO to ENTITY and viceversa
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

@Mapper
public interface ProductMapper {

    /**
     * Map the product data transfer to entity
     *
     * @param productDTO product data to map
     * @return an instance of {@link Product}
     */
    Product toDocument(ProductDTO productDTO);

    /**
     * Map the product entity to data transfer
     *
     * @param product product data to map
     * @return an instance of {@link ProductDTO}
     */
    ProductDTO toDTO(Product product);
}
