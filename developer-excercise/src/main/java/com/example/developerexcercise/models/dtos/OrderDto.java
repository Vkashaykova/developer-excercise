package com.example.developerexcercise.models.dtos;

import java.util.List;

public class OrderDto {

    List<String> products;

    public OrderDto() {
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }
}
