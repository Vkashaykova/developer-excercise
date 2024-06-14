package com.example.developerexcercise.services;

import com.example.developerexcercise.exseptions.DuplicateEntityException;
import com.example.developerexcercise.exseptions.EntityNotFoundException;
import com.example.developerexcercise.models.Product;
import com.example.developerexcercise.repositories.contracts.ProductRepository;
import com.example.developerexcercise.services.contracts.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing products.
 */
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    /**
     * Constructs an instance of ProductServiceImpl with dependencies injected.
     *
     * @param productRepository The repository handling product data operations.
     */
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves all products.
     *
     * @return A list of all Product objects.
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId The ID of the product to retrieve.
     * @return The Product object with the given ID.
     * @throws EntityNotFoundException if the product with the given ID is not found.
     */
    @Override
    public Product getProductById(int productId) {

        return productRepository.getProductById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product", "id", String.valueOf(productId)));
    }

    /**
     * Retrieves a product by its name.
     *
     * @param name The name of the product to retrieve.
     * @return An Optional containing the Product object if found, or empty if not found.
     */
    @Override
    public Optional<Product> getProductByName(String name) {
        return productRepository.getProductByName(name);
    }

    /**
     * Adds a new product.
     *
     * @param product The Product object to add.
     * @throws DuplicateEntityException if a product with the same name already exists.
     */
    @Override
    public void addProduct(Product product) {
        Optional<Product> existingProduct = productRepository.getProductByName(product.getProductName());
        if (existingProduct.isPresent()) {
            throw new DuplicateEntityException("Product", "name", product.getProductName());
        }
        productRepository.addProduct(product);
    }

    /**
     * Updates an existing product.
     *
     * @param product The Product object containing updated details.
     * @throws DuplicateEntityException if a product with the same name and price already exists.
     */
    @Override
    public void updateProduct(Product product) {
        Optional<Product> existingProduct = productRepository.getProductById(product.getProductId());
        if (existingProduct.isPresent() && product.getPrice() == existingProduct.get().getPrice()) {
            throw new DuplicateEntityException("Product", "name", product.getProductName());
        }
        productRepository.updateProduct(product);
    }

    /**
     * Deletes a product.
     *
     * @param product The Product object to delete.
     */
    @Override
    public void deleteProduct(Product product) {
        productRepository.deleteProduct(product);
    }
}
