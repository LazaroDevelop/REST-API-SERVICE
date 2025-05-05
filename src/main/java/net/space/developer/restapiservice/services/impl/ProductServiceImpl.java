package net.space.developer.restapiservice.services.impl;

import lombok.RequiredArgsConstructor;
import net.space.developer.restapiservice.common.exception.ProductNotFoundException;
import net.space.developer.restapiservice.common.utility.ApplicationConstants;
import net.space.developer.restapiservice.documents.Product;
import net.space.developer.restapiservice.mapper.ProductMapper;
import net.space.developer.restapiservice.model.ProductDTO;
import net.space.developer.restapiservice.repository.ProductRepository;
import net.space.developer.restapiservice.services.ProductService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Product service class to implements the products features
 *
 * @author Lazaro Noel Guerra Medina
 * @since 2025-04-29
 */

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    /**
     * Product mapper injection
     */
    private final ProductMapper productMapper;

    /**
     * Product repository injection
     */
    private final ProductRepository productRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductDTO> findAllProducts() {
        List<Product> products = productRepository.findAll();

        return products
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value = ApplicationConstants.PRODUCTS_CACHE)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Product> pageProducts = productRepository.findAll(pageable);

        var result = pageProducts.getContent()
                .stream()
                .map(productMapper::toDTO)
                .toList();

        return new PageImpl<>(result, pageable, pageProducts.getTotalElements());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value = ApplicationConstants.PRODUCTS_CACHE)
    public Page<ProductDTO> getProductsByCategory(String category, Pageable pageable) {
        Page<Product> pageProduct = productRepository.findByCategory(category, pageable);

        var result = pageProduct.getContent()
                .stream()
                .map(productMapper::toDTO)
                .toList();

        return new PageImpl<>(result, pageable, pageProduct.getTotalElements());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value = ApplicationConstants.PRODUCTS_CACHE)
    public Page<ProductDTO> getProductsByName(String productName, Pageable pageable) {
        Page<Product> pageProduct = productRepository.findByNameContainingIgnoreCase(productName, pageable);

        var result = pageProduct.getContent()
                .stream()
                .map(productMapper::toDTO)
                .toList();

        return new PageImpl<>(result, pageable, pageProduct.getTotalElements());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value = ApplicationConstants.PRODUCT_CACHE, key = "#id")
    public ProductDTO getProductById(String id) {
        Optional<Product> opt = productRepository.findById(id);

        if (opt.isPresent()) {
            return productMapper.toDTO(opt.get());
        }

        throw new ProductNotFoundException(ApplicationConstants.PRODUCT_NOT_FOUND_MESSAGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductDTO createProduct(ProductDTO product) {

        Product newProduct = productMapper.toDocument(product);

        Product createdProduct = productRepository.save(newProduct);

        return productMapper.toDTO(createdProduct);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @CachePut(value = ApplicationConstants.PRODUCT_CACHE, key = "#id",unless = "#result == null")
    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        Optional<Product> opt = productRepository.findById(id);

        if (opt.isPresent()) {
            Product foundProduct = getFoundProduct(productDTO, opt);

            Product updatedProduct = productRepository.save(foundProduct);

            return productMapper.toDTO(updatedProduct);
        }

        throw new ProductNotFoundException(ApplicationConstants.PRODUCT_NOT_FOUND_MESSAGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(value = ApplicationConstants.PRODUCT_CACHE, key = "#id",allEntries = true)
    public boolean deleteProduct(String id) {
        Optional<Product> opt = productRepository.findById(id);

        if(opt.isPresent()) {
            productRepository.delete(opt.get());
            return true;
        }

        return false;
    }

    /**
     * Get the found product and update the information
     *
     * @param productDTO the new product information
     * @param opt an {@link Optional} of {@link ProductDTO}
     * @return the instance of {@link Product} with all the information
     */
    private Product getFoundProduct(ProductDTO productDTO, Optional<Product> opt) {
        Product product = opt.get();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setState(productDTO.getState());
        product.setBatch(productDTO.getBatch());
        product.setImages(productDTO.getImages());
        product.setBrand(productDTO.getBrand());
        return product;
    }
}
