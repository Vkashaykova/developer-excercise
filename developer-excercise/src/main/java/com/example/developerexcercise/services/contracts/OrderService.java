package com.example.developerexcercise.services.contracts;

import com.example.developerexcercise.models.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();

    Order getOrderById(int orderId);

    List<Order> getOrdersByProduct(String productName);

    int createOrder(Order order);

    int updateOrder(Order order);

    void deleteOrder(Order order);


}
