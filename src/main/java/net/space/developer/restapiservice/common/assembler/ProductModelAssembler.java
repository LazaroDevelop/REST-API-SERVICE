package net.space.developer.restapiservice.common.assembler;

import net.space.developer.restapiservice.controller.ProductController;
import net.space.developer.restapiservice.model.ProductDTO;
import net.space.developer.restapiservice.model.QueryModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<ProductDTO, EntityModel<ProductDTO>> {

    @Override
    public EntityModel<ProductDTO> toModel(ProductDTO product) {
        return EntityModel.of(product,
                linkTo(methodOn(ProductController.class).getProductById(product.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAllProducts()).withRel("all"),
                linkTo(methodOn(ProductController.class).getAllProductsWithPage(0, 10)).withRel("all/paged"),
                linkTo(methodOn(ProductController.class).getAllProductsWithPageAndSorting(0, 10, new ArrayList<>())).withRel("all/paged&sorting"),
                linkTo(methodOn(ProductController.class).getAllProductsWithPageAndSortingByCategory(0, 10, new QueryModel(product.getCategory().name()))).withRel("all/page-by-category"),
                linkTo(methodOn(ProductController.class).getAllProductsWithPageAndSortingByName(0, 10, new QueryModel(product.getName()))).withRel("all/paged-by-name"),
                linkTo(methodOn(ProductController.class).addProduct(product)).withRel("create"),
                linkTo(methodOn(ProductController.class).updateProduct(product.getId(), product)).withRel("update"),
                linkTo(methodOn(ProductController.class).deleteProduct(product.getId())).withRel("delete")
        );
    }
}
