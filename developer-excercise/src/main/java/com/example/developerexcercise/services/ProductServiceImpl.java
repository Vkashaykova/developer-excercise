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

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public Product getProductById(int productId) {

        return productRepository.getProductById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product", "id", String.valueOf(productId)));
    }

    @Override
    public Optional<Product> getProductByName(String name) {
        return productRepository.getProductByName(name);
    }

    @Override
    public void addProduct(Product product) {
        Optional<Product> existingProduct = productRepository.getProductByName(product.getProductName());
        if (existingProduct.isPresent()) {
            throw new DuplicateEntityException("Product");
        }
        productRepository.addProduct(product);
    }


    @Override
    public void updateProduct(Product product) {
        Optional<Product> existingProduct = productRepository.getProductById(product.getProductId());
        if (existingProduct.isPresent()) {
            throw new DuplicateEntityException("Product");
        }
        productRepository.updateProduct(product);
    }

    @Override
    public void deleteProduct(Product product) {
         productRepository.deleteProduct(product);
    }
}
