package net.space.developer.restapiservice.mapper;

import net.space.developer.restapiservice.documents.Product;
import net.space.developer.restapiservice.model.ProductDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
    Product toDocument(ProductDTO productDTO);

    ProductDTO toDTO(Product product);
}
