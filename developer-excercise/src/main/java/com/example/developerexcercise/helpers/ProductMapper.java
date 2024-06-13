package com.example.developerexcercise.helpers;

import com.example.developerexcercise.models.Order;
import com.example.developerexcercise.models.Product;
import com.example.developerexcercise.models.dtos.OrderDto;
import com.example.developerexcercise.models.dtos.ProductDto;
import com.example.developerexcercise.services.contracts.ProductService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ProductMapper {
    private final ProductService productService;

    public ProductMapper(ProductService productService) {
        this.productService = productService;
    }

    public Product fromDto(int id, ProductDto dto) {
        Product product = fromDto(dto);
        product.setProductId(id);

        return product;

    }

    public Product fromDto(ProductDto dto) {
        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setPrice(dto.getPrice());
        product.setCurrency(dto.getCurrency());

        return product;

    }
}
