package com.example.developerexcercise;

import com.example.developerexcercise.models.Order;
import com.example.developerexcercise.models.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class helpers {
    public static Product createMockProduct() {
        Product mockProduct = new Product();
        mockProduct.setProductId(1);
        mockProduct.setProductName("Mock Product");
        mockProduct.setPrice(100);
        mockProduct.setCurrency("aws");
        return mockProduct;
    }

    public static Product createMockProduct2() {
        Product mockProduct = new Product();
        mockProduct.setProductId(1);
        mockProduct.setProductName("Mock Product2");
        mockProduct.setPrice(50);
        mockProduct.setCurrency("aws");
        return mockProduct;
    }

    public static Product createMockProduct3() {
        Product mockProduct = new Product();
        mockProduct.setProductId(1);
        mockProduct.setProductName("Mock Product3");
        mockProduct.setPrice(75);
        mockProduct.setCurrency("aws");
        return mockProduct;
    }

    public static Product createUpdatedMockProduct(Product productToUpdate) {
        Product updatedProduct = new Product();
        updatedProduct.setProductId(productToUpdate.getProductId());
        updatedProduct.setProductName(productToUpdate.getProductName());
        updatedProduct.setPrice(productToUpdate.getPrice() + 100);
        updatedProduct.setCurrency(productToUpdate.getCurrency());
        return updatedProduct;
    }

    public static Order createMockOrder() {
        Order mockOrder = new Order();
        mockOrder.setOrderId(1);
        mockOrder.setTotalPrice(200);
        mockOrder.setTimestamp(LocalDateTime.now());

        List<Product> products = new ArrayList<>();
        products.add(createMockProduct());
        mockOrder.setProducts(products);

        return mockOrder;
    }
}
