package com.example.developerexcercise.repositories.contracts;

import com.example.developerexcercise.models.Order;

import java.util.List;

public interface OrderRepository {
    List<Order> getAllOrders();

    Order getOrderById(int orderId);

    List<Order> getOrdersByProduct(int productId);

    void createOrder(Order order);

    void updateOrder(Order order);

    void deleteOrder(Order order);
}
