package com.example.developerexcercise.helpers;

import com.example.developerexcercise.models.Order;
import com.example.developerexcercise.models.Product;
import com.example.developerexcercise.models.dtos.OrderDto;
import com.example.developerexcercise.services.contracts.ProductService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class OrderMapper {

    private final ProductService productService;

    public OrderMapper(ProductService productService) {
        this.productService = productService;
    }

    public Order fromDto(int id, OrderDto dto) {
        Order order = fromDto(dto);
        order.setOrderId(id);

        return order;
    }

    public Order fromDto(OrderDto dto) {
        Order order = new Order();

        for (String currentProduct : dto.getProducts()) {
            Optional<Product> product = productService.getProductByName(currentProduct);
            product.ifPresent(value -> order.getProducts().add(value));
        }
        order.setTimestamp(LocalDateTime.now());

        return order;
    }
}
