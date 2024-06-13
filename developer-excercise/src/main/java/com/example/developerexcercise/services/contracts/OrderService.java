package com.example.developerexcercise.services.contracts;

import com.example.developerexcercise.models.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();

    Order getOrderById(int orderId);

    List<Order> getOrdersByProduct(int productId);

    double createOrder(Order order);

    void updateOrder(Order order);

    void deleteOrder(Order order);


}
