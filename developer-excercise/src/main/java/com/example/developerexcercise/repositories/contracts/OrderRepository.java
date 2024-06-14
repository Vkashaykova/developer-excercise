package com.example.developerexcercise.repositories.contracts;

import com.example.developerexcercise.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<Order> getAllOrders();

    Optional<Order> getOrderById(int orderId);

    Optional<List<Order>> getOrdersByProduct(int productId);

    void createOrder(Order order);

    void updateOrder(Order order);

    void deleteOrder(Order order);
}
