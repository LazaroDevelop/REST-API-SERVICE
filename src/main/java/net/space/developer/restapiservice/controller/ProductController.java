package net.space.developer.restapiservice.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.space.developer.restapiservice.common.assembler.ProductModelAssembler;
import net.space.developer.restapiservice.model.ProductDTO;
import net.space.developer.restapiservice.model.QueryModel;
import net.space.developer.restapiservice.services.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Product controller to manage all the products endpoints
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    /**
     * Inject the product service instance
     */
    private final ProductService productService;

    /**
     * Inject the product model assembler instance
     */
    private final ProductModelAssembler productModelAssembler;

    /**
     * Get all the product in the system
     *
     * @return a {@link ResponseEntity} with the list of the products, if the list is empty or null the http status code will be 204
     */
    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EntityModel<ProductDTO>>> getAllProducts() {
        List<ProductDTO> response = productService.findAllProducts();

        return getCollectionModelResponseEntity(response);
    }


    /**
     * Get all the product in the system with pagination
     *
     * @param pageNumber the page number
     * @param pageSize the size of the page
     * @return a {@link ResponseEntity} with the list of the products, if the list is empty or null the http status code will be 204
     */
    @GetMapping("/all-pagination")
    public ResponseEntity<CollectionModel<EntityModel<ProductDTO>>> getAllProductsWithPage(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return getCollectionModelResponseEntity(pageRequest);
    }

    /**
     * Get all the product in the system with pagination and sorting
     *
     * @param pageNumber the page number
     * @param pageSize the page size
     * @param sort the sort information like field of the sort and direction of the sort
     * @return a {@link ResponseEntity} with the list of the products, if the list is empty or null the http status code will be 204
     */
    @GetMapping("/all-pagination-sorting")
    public ResponseEntity<CollectionModel<EntityModel<ProductDTO>>> getAllProductsWithPageAndSorting(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            @RequestParam(value = "sort", defaultValue = "id, asc") List<String> sort
    ){
        String field = sort.getFirst();
        String sortDirection = sort.getLast();

        Sort.Direction direction = "asc".equals(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(direction, field));

        return getCollectionModelResponseEntity(pageRequest);
    }

    /**
     * Get all the product with pagination and sorting by a given name
     *
     * @param pageNumber the page number
     * @param pageSize the page size
     * @param queryModel the product query with the name
     * @return a {@link ResponseEntity} with the list of the products, if the list is empty or null the http status code will be 204
     */
    @PostMapping("/all-pagination/by-name")
    public ResponseEntity<CollectionModel<EntityModel<ProductDTO>>> getAllProductsWithPageAndSortingByName(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            @RequestBody QueryModel queryModel
    ){
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        List<ProductDTO> response = productService.getProductsByName(queryModel.getQuery(), pageRequest);

        return getCollectionModelResponseEntity(response);
    }

    /**
     * Get all the products with pagination and sorting by a given category
     *
     * @param pageNumber the page number
     * @param pageSize the page size
     * @param queryModel the query with the category
     * @return a {@link ResponseEntity} with the list of the products, if the list is empty or null the http status code will be 204
     */
    @PostMapping("/all-pagination/by-category")
    public ResponseEntity<CollectionModel<EntityModel<ProductDTO>>> getAllProductsWithPageAndSortingByCategory(
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            @RequestBody QueryModel queryModel
    ){
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        List<ProductDTO> response = productService.getProductsByCategory(queryModel.getQuery(), pageRequest);

        if(Objects.isNull(response) || response.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return getCollectionModelResponseEntity(response);
    }

    /**
     * Create a new product
     *
     * @param productDTO the new product information
     * @return the saved product instance in the system
     */
    @PostMapping("/add")
    public ResponseEntity<EntityModel<ProductDTO>> addProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO response = productService.createProduct(productDTO);

        if(Objects.isNull(response)){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productModelAssembler.toModel(response));
    }

    /**
     * Get one product by identifier
     *
     * @param id the given product identifier
     * @return the found product
     */
    @GetMapping("/by-id/{id}")
    public ResponseEntity<EntityModel<ProductDTO>> getProductById(@PathVariable("id") String id) {

        ProductDTO response = productService.getProductById(id);

        if(Objects.isNull(response)){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productModelAssembler.toModel(response));
    }

    /**
     * Update an existing product by identifier
     *
     * @param id the given product identifier
     * @param productDTO the information to update
     * @return the updated product information
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<EntityModel<ProductDTO>> updateProduct(@PathVariable("id") String id, @RequestBody ProductDTO productDTO) {
        ProductDTO response = productService.updateProduct(id, productDTO);

        if(Objects.isNull(response)){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productModelAssembler.toModel(response));
    }

    /**
     * Drop or delete an existing product by identifier
     *
     * @param id the given identifier
     * @return 204 if was deleted, 404 in other case
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable("id") String id) {
        boolean result = productService.deleteProduct(id);

        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Retrieve the list of product from the page request
     *
     * @param pageRequest the page request information
     * @return an instance of {@link ResponseEntity} with the collection model
     */
    @NotNull
    private ResponseEntity<CollectionModel<EntityModel<ProductDTO>>> getCollectionModelResponseEntity(PageRequest pageRequest) {

        List<ProductDTO> response = productService.getAllProducts(pageRequest);

        return getCollectionModelResponseEntity(response);
    }

    /**
     * Check the result list if is empty build a not content information, in other case return the list with the collection model
     *
     * @param response the list of products
     * @return an instance of {@link ResponseEntity} with the result information
     */
    @NotNull
    private ResponseEntity<CollectionModel<EntityModel<ProductDTO>>> getCollectionModelResponseEntity(List<ProductDTO> response) {
        if(Objects.isNull(response) || response.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        var pageProduct = response.stream().map(productModelAssembler::toModel).toList();

        return ResponseEntity.status(HttpStatus.OK).body(CollectionModel.of(pageProduct,
                linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel()));
    }
}
