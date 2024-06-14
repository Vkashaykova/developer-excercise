package com.example.developerexcercise.helpers;

import com.example.developerexcercise.models.Product;
import com.example.developerexcercise.models.dtos.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

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
