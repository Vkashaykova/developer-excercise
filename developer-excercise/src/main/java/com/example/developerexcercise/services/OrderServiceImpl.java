package com.example.developerexcercise.services;

import com.example.developerexcercise.models.Order;
import com.example.developerexcercise.models.Product;
import com.example.developerexcercise.repositories.contracts.OrderRepository;
import com.example.developerexcercise.services.contracts.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return null;
    }

    @Override
    public Order getOrderById(int orderId) {
        return null;
    }

    @Override
    public List<Order> getOrdersByProduct(int productId) {
        return null;
    }

    public double createOrder(Order order) {
        List<Product> products = order.getProducts();
        double totalPrice = 0;

        if (products.size() >= 3) {
            totalPrice = calculateTwoForThreeDeal(products);
            if (products.size() > 3) {
                totalPrice += calculateBuyOneGetOneHalfPrice(products.subList(3, products.size()));
            }
        } else {
            totalPrice = calculateBuyOneGetOneHalfPrice(products);
        }

        order.setTotalPrice(totalPrice);
        orderRepository.createOrder(order);
        return order.getTotalPrice();
    }

    private double calculateTwoForThreeDeal(List<Product> products) {
        double totalPrice = 0;
        double minValue = Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            double price = products.get(i).getPrice();
            totalPrice += price;
            if (price < minValue) {
                minValue = price;
            }
        }

        return totalPrice - minValue;
    }

    private double calculateBuyOneGetOneHalfPrice(List<Product> products) {
        double totalPrice = 0;
        Map<String, Integer> productCount = new HashMap<>();

        // Count the number of each product
        for (Product product : products) {
            String productName = product.getProductName();
            productCount.put(productName, productCount.getOrDefault(productName, 0) + 1);
        }

        // Calculate the total price applying the buy 1 get 1 50% off deal
        for (Product product : products) {
            String productName = product.getProductName();
            double productPrice = product.getPrice();
            int count = productCount.get(productName);

            if (count > 0) {
                int pairs = count / 2;
                int remainder = count % 2;

                totalPrice += pairs * (productPrice + (productPrice / 2));
                totalPrice += remainder * productPrice;

                // Reduce the count to avoid recounting in the next iteration
                productCount.put(productName, 0);
            }
        }

        return totalPrice;
    }

    @Override
    public void updateOrder(Order order) {

    }

    @Override
    public void deleteOrder(Order order) {

    }
}
