package com.example.developerexcercise.services.contracts;

import com.example.developerexcercise.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(int productId);

    Optional<Product> getProductByName(String name);

    void addProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(Product product);
}

