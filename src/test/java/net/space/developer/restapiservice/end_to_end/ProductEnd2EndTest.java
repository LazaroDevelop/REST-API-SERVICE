package net.space.developer.restapiservice.end_to_end;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.space.developer.restapiservice.common.assembler.ProductModelAssembler;
import net.space.developer.restapiservice.controller.ProductController;
import net.space.developer.restapiservice.documents.Product;
import net.space.developer.restapiservice.documents.enums.Category;
import net.space.developer.restapiservice.documents.enums.State;
import net.space.developer.restapiservice.model.ProductDTO;
import net.space.developer.restapiservice.model.QueryModel;
import net.space.developer.restapiservice.services.ProductService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Product controller end-to-end testing class
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

@WebMvcTest(controllers = ProductController.class)
class ProductEnd2EndTest {

    /**
     * Mock the product service
     */
    @MockitoBean
    ProductService productService;

    /**
     * Mock the product model assembler
     */
    @MockitoBean
    ProductModelAssembler productModelAssembler;

    /**
     * Inject the Mock MVC instance
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Inject the object mapper instance
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Testing initialization
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the create feature through the endpoints
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Should create a product with http status 201")
    void shouldCreateProduct201() throws Exception {
        Product product = createProduct();

        String content = objectMapper.writeValueAsString(product);

        var dto = new ProductDTO();

        BeanUtils.copyProperties(product, dto);

        when(productService.createProduct(any(ProductDTO.class))).thenReturn(dto);

        mockMvc.perform(
                post("/api/v1/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        verify(productService, times(1)).createProduct(any(ProductDTO.class));
    }

    /**
     * Test the create feature through the endpoints expect the worst case
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Shouldn't create a product with status 204")
    void shouldNotCreateProduct204() throws Exception {
        Product product = createProduct();

        String content = objectMapper.writeValueAsString(product);

        var dto = new ProductDTO();

        BeanUtils.copyProperties(product, dto);

        when(productService.createProduct(any(ProductDTO.class))).thenReturn(null);

        mockMvc.perform(
                        post("/api/v1/products/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(productService, times(1)).createProduct(any(ProductDTO.class));
    }

    /**
     * Test the find all feature through the endpoints
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Should find all products with status 200")
    void shouldFindAllProducts200() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);

        var list = List.of(dto);

        var model = EntityModel.of(dto);

        when(productModelAssembler.toModel(any(ProductDTO.class))).thenReturn(model);

        when(productService.findAllProducts()).thenReturn(list);

        mockMvc.perform(
                get("/api/v1/products/all")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.productDTOList", hasSize(1)))
        .andExpect(jsonPath("$._embedded.productDTOList[0].id", is(dto.getId())))
        .andDo(print());

        verify(productService, times(1)).findAllProducts();
        verify(productModelAssembler, times(1)).toModel(any(ProductDTO.class));
    }

    /**
     * Test the find all feature through the endpoints expect the worst case
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Shouldn't find all products with status 204")
    void shouldNotFindAllProducts204() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);

        when(productService.findAllProducts()).thenReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/api/v1/products/all")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(productService, times(1)).findAllProducts();
    }

    /**
     * Test the find all paginated feature through the endpoints
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Should find all product paginated with status 200")
    void shouldFindAllProductsPaginated200() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);
        var list = List.of(dto);

        var model = EntityModel.of(dto);

        when(productModelAssembler.toModel(any(ProductDTO.class))).thenReturn(model);

        when(productService.getAllProducts(any(Pageable.class))).thenReturn(list);

        mockMvc.perform(
                get("/api/v1/products/all-pagination")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.productDTOList", hasSize(1)))
        .andExpect(jsonPath("$._embedded.productDTOList[0].id", is(dto.getId())))
        .andDo(print());

        verify(productService, times(1)).getAllProducts(any(Pageable.class));
        verify(productModelAssembler, times(1)).toModel(any(ProductDTO.class));
    }

    /**
     * Test the find all paginated feature through the endpoints expect the worst case
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Shouldn't find all products with status 204")
    void shouldNotFindAllProductsPaginated204() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);

        when(productService.getAllProducts(any(Pageable.class))).thenReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/api/v1/products/all-pagination")
                                .param("page", "0")
                                .param("size", "10")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(productService, times(1)).getAllProducts(any(Pageable.class));
    }

    /**
     * Test the find all paginated and sorted feature through the endpoints
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Should find all products with paging and sorting and status 200")
    void shouldFindAllProductsPaginatedAndSorted200() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);

        var list = List.of(dto);

        var model = EntityModel.of(dto);

        when(productModelAssembler.toModel(any(ProductDTO.class))).thenReturn(model);

        when(productService.getAllProducts(any(Pageable.class))).thenReturn(list);

        mockMvc.perform(
                get("/api/v1/products/all-pagination-sorting")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id, asc")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.productDTOList[0].id", is(dto.getId())))
                .andDo(print());

        verify(productService, times(1)).getAllProducts(any(Pageable.class));
        verify(productModelAssembler, times(1)).toModel(any(ProductDTO.class));
    }

    /**
     * Test the find all paginated and sorted feature through the endpoints expect the worst case
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Shouldn't find all products with paging and sorting and status 200")
    void shouldNotFindAllProductsPaginatedAndSorted204() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);

        when(productService.getAllProducts(any(Pageable.class))).thenReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/api/v1/products/all-pagination-sorting")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "id, asc")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(productService, times(1)).getAllProducts(any(Pageable.class));
    }

    /**
     * Test the find all paginated and sorted by name feature through the endpoints
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Should find all products paginated by name with status 200")
    void shouldFindAllProductsPaginatedByName200() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);

        var list = List.of(dto);
        var model = EntityModel.of(dto);

        QueryModel query = new QueryModel("Product 1");

        when(productModelAssembler.toModel(any(ProductDTO.class))).thenReturn(model);
        when(productService.getProductsByName(anyString(), any(Pageable.class))).thenReturn(list);

        mockMvc.perform(
                post("/api/v1/products/all-pagination/by-name")
                        .param("page", "0")
                        .param("size", "10")
                        .content(objectMapper.writeValueAsString(query))
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.productDTOList", hasSize(1)))
        .andExpect(jsonPath("$._embedded.productDTOList[0].id", is(dto.getId())))
        .andDo(print());

        verify(productService, times(1)).getProductsByName(anyString(), any(Pageable.class));
        verify(productModelAssembler, times(1)).toModel(any(ProductDTO.class));
    }

    /**
     * Test the find all paginated and sorted by name feature through the endpoints expect the worst case
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Shouldn't find all products paginated by name with status 204")
    void shouldNotFindAllProductsPaginatedByName204() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);

        QueryModel model = new QueryModel("Product 1");

        when(productService.getProductsByName(anyString(), any(Pageable.class))).thenReturn(new ArrayList<>());

        mockMvc.perform(
                        post("/api/v1/products/all-pagination/by-name")
                                .param("page", "0")
                                .param("size", "10")
                                .content(objectMapper.writeValueAsString(model))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(productService, times(1)).getProductsByName(anyString(), any(Pageable.class));
    }

    /**
     * Test the find all paginated and sorted by category feature through the endpoints
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Should find all products paginated by category with status 200")
    void shouldFindAllProductsPaginatedByCategory200() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);

        var list = List.of(dto);
        var model = EntityModel.of(dto);

        QueryModel query = new QueryModel(Category.CLOTHES.name());

        when(productModelAssembler.toModel(any(ProductDTO.class))).thenReturn(model);
        when(productService.getProductsByCategory(anyString(), any(Pageable.class))).thenReturn(list);

        mockMvc.perform(
                post("/api/v1/products/all-pagination/by-category")
                        .param("page", "0")
                        .param("size", "10")
                        .content(objectMapper.writeValueAsString(query))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.productDTOList[0].id", is(dto.getId())))
                .andDo(print());

        verify(productService, times(1)).getProductsByCategory(anyString(), any(Pageable.class));
        verify(productModelAssembler, times(1)).toModel(any(ProductDTO.class));
    }

    /**
     * Test the find all paginated and sorted by category feature through the endpoints expect the worst case
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Shouldn't find all products paginated by category with status 204")
    void shouldNotFindAllProductsPaginatedByCategory204() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);

        QueryModel model = new QueryModel(Category.CLOTHES.name());

        when(productService.getProductsByCategory(anyString(), any(Pageable.class))).thenReturn(new ArrayList<>());

        mockMvc.perform(
                        post("/api/v1/products/all-pagination/by-category")
                                .param("page", "0")
                                .param("size", "10")
                                .content(objectMapper.writeValueAsString(model))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(productService, times(1)).getProductsByCategory(anyString(), any(Pageable.class));
    }

    /**
     * Test the find by id feature through the endpoints
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Should find product by identifier with status 200")
    void shouldFindProductById200() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);

        when(productService.getProductById(anyString())).thenReturn(dto);

        mockMvc.perform(
                get(MessageFormat.format("/api/v1/products/by-id/{0}", product.getId()))
                        .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());

        verify(productService, times(1)).getProductById(anyString());
    }

    /**
     * Test the find by id feature through the endpoints expect the worst case
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Shouldn't find product by identifier with status 404")
    void shouldNotFindProductById404() throws Exception {
        var product = createProduct();

        when(productService.getProductById(anyString())).thenReturn(null);

        mockMvc.perform(
                        get(MessageFormat.format("/api/v1/products/by-id/{0}", product.getId()))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(productService, times(1)).getProductById(anyString());
    }

    /**
     * Test the update feature through the endpoints
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Should update product with status 200")
    void shouldUpdateProduct200() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);

        dto.setName("Product updated");
        dto.setBatch("Product updated batch");
        dto.setBrand("Product updated brand");
        dto.setCategory(Category.HOME_APPLIANCES);
        dto.setState(State.OUT_STOCK);
        dto.setDescription("The product was updated recently");

        var model = EntityModel.of(dto);

        when(productModelAssembler.toModel(any(ProductDTO.class))).thenReturn(model);

        when(productService.updateProduct(anyString(), any(ProductDTO.class))).thenReturn(dto);

        mockMvc.perform(
                put(MessageFormat.format("/api/v1/products/update/{0}", product.getId()))
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print());

        verify(productModelAssembler, times(1)).toModel(any(ProductDTO.class));
        verify(productService, times(1)).updateProduct(anyString(), any(ProductDTO.class));
    }

    /**
     * Test the update feature through the endpoints expect the worst case
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Shouldn't update product with status 404")
    void shouldNotUpdateProduct404() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);

        dto.setName("Product updated");
        dto.setBatch("Product updated batch");
        dto.setBrand("Product updated brand");
        dto.setCategory(Category.HOME_APPLIANCES);
        dto.setState(State.OUT_STOCK);
        dto.setDescription("The product was updated recently");

        when(productService.updateProduct(anyString(), any(ProductDTO.class))).thenReturn(null);

        mockMvc.perform(
                        put(MessageFormat.format("/api/v1/products/update/{0}", product.getId()))
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(productService, times(1)).updateProduct(anyString(), any(ProductDTO.class));
    }

    /**
     * Test the delete feature through the endpoints
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Should delete a product with status 200")
    void shouldDeleteProduct200() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);

        when(productService.deleteProduct(anyString())).thenReturn(true);

        mockMvc.perform(
                delete(MessageFormat.format("/api/v1/products/delete/{0}", product.getId()))
        )
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(productService, times(1)).deleteProduct(anyString());
    }

    /**
     * Test the delete feature through the endpoints expect the worst case
     *
     * @throws Exception this exception can be thrown by Mock MVC class
     */
    @Test
    @DisplayName("Shouldn't delete a product with status 404")
    void shouldNotDeleteProduct404() throws Exception {
        var dto = new ProductDTO();
        var product = createProduct();

        BeanUtils.copyProperties(product, dto);

        when(productService.deleteProduct(anyString())).thenReturn(false);

        mockMvc.perform(
                        delete(MessageFormat.format("/api/v1/products/delete/{0}", product.getId()))
                )
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(productService, times(1)).deleteProduct(anyString());
    }

    /**
     * Create an instance of {@link Product}
     *
     * @return a product with mock data information
     */
    private Product createProduct(){
        Product product = new Product();
        product.setId(new ObjectId().toString());
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
}
