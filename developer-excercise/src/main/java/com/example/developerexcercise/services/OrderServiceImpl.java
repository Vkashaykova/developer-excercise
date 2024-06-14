package com.example.developerexcercise.services;

import com.example.developerexcercise.exseptions.EntityNotFoundException;
import com.example.developerexcercise.models.Order;
import com.example.developerexcercise.models.Product;
import com.example.developerexcercise.repositories.contracts.OrderRepository;
import com.example.developerexcercise.services.contracts.OrderService;
import com.example.developerexcercise.services.contracts.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    @Override
    public Order getOrderById(int orderId) {

        return orderRepository.getOrderById(orderId)
                .orElseThrow(() -> new EntityNotFoundException
                        ("Order", "id", String.valueOf(orderId)));
    }

    @Override
    public List<Order> getOrdersByProduct(String productName) {
        Product product = productService.getProductByName(productName).get();

        return orderRepository.getOrdersByProduct(product.getProductId())
                .orElseThrow(() -> new EntityNotFoundException
                        ("order", "product", productName));
    }

    public int createOrder(Order order) {
        int totalPrice = calculatePrice(order);
        order.setTotalPrice(totalPrice);
        orderRepository.createOrder(order);

        return order.getTotalPrice();
    }

    @Override
    public int updateOrder(Order order) {
        int totalPrice = calculatePrice(order);
        order.setTotalPrice(totalPrice);
        orderRepository.updateOrder(order);

        return order.getTotalPrice();
    }

    @Override
    public void deleteOrder(Order order) {
        orderRepository.deleteOrder(order);

    }


    private int calculatePrice(Order order) {
        List<Product> products = order.getProducts();
        int totalPrice;

        if (products.size() >= 3) {
            totalPrice = calculateTwoForThreeDeal(products);
            if (products.size() > 3) {
                totalPrice += calculateBuyOneGetOneHalfPrice(products.subList(3, products.size()));
            }
        } else {
            totalPrice = calculateBuyOneGetOneHalfPrice(products);
        }

        return totalPrice;
    }

    private int calculateTwoForThreeDeal(List<Product> products) {
        int totalPrice = 0;
        int minValue = Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            int price = products.get(i).getPrice();
            totalPrice += price;
            if (price < minValue) {
                minValue = price;
            }
        }

        return totalPrice - minValue;
    }

    private int calculateBuyOneGetOneHalfPrice(List<Product> products) {
        int totalPrice = 0;
        Map<String, Integer> productCount = new HashMap<>();

        for (Product product : products) {
            String productName = product.getProductName();
            productCount.put(productName, productCount.getOrDefault(productName, 0) + 1);
        }

        for (Product product : products) {
            String productName = product.getProductName();
            int productPrice = product.getPrice();
            int count = productCount.get(productName);

            if (count > 0) {
                int pairs = count / 2;
                int remainder = count % 2;

                totalPrice += pairs * (productPrice + (productPrice / 2));
                totalPrice += remainder * productPrice;

                productCount.put(productName, 0);
            }
        }

        return totalPrice;
    }

}
