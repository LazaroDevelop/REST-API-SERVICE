package net.space.developer.restapiservice.services.impl;

import lombok.RequiredArgsConstructor;
import net.space.developer.restapiservice.common.exceptions.ProductNotFoundException;
import net.space.developer.restapiservice.common.utility.ApplicationConstants;
import net.space.developer.restapiservice.documents.Product;
import net.space.developer.restapiservice.mapper.ProductMapper;
import net.space.developer.restapiservice.model.ProductDTO;
import net.space.developer.restapiservice.repository.ProductRepository;
import net.space.developer.restapiservice.services.ProductService;
import org.springframework.data.domain.Page;
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

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Product> pageProducts = productRepository.findAll(pageable);

        return pageProducts.getContent()
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductDTO> getProductsByCategory(String category, Pageable pageable) {
        Page<Product> pageProduct = productRepository.findByCategory(category, pageable);

        return pageProduct.getContent()
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductDTO> getProductsByName(String productName, Pageable pageable) {
        Page<Product> pageProduct = productRepository.findByNameContainingIgnoreCase(productName, pageable);

        return pageProduct.getContent()
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * @param productDTO the new product information
     * @param opt an {@link Optional} of {@link ProductDTO}
     * @return
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
