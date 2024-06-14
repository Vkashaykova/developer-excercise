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

/**
 * Implementation of OrderService providing business logic operations for orders.
 */
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;

    /**
     * Constructs an instance of OrderServiceImpl with dependencies injected.
     *
     * @param orderRepository The repository handling order data access.
     * @param productService  The service handling product operations.
     */
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    /**
     * Retrieves all orders.
     *
     * @return List of all Order objects.
     */
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId ID of the order to retrieve.
     * @return The requested Order object.
     * @throws EntityNotFoundException if the order with the given ID is not found.
     */
    @Override
    public Order getOrderById(int orderId) {

        return orderRepository.getOrderById(orderId)
                .orElseThrow(() -> new EntityNotFoundException
                        ("Order", "id", String.valueOf(orderId)));
    }

    /**
     * Retrieves orders containing a specific product.
     *
     * @param productName Name of the product to filter orders by.
     * @return List of orders containing the specified product.
     * @throws EntityNotFoundException if no orders contain the specified product.
     */
    @Override
    public List<Order> getOrdersByProduct(String productName) {
        Product product = productService.getProductByName(productName).get();

        return orderRepository.getOrdersByProduct(product.getProductId())
                .orElseThrow(() -> new EntityNotFoundException
                        ("order", "product", productName));
    }

    /**
     * Creates a new order and calculates the total price based on product prices and offers.
     *
     * @param order Order object to create.
     * @return Total price of the created order.
     */
    public int createOrder(Order order) {
        int totalPrice = calculatePrice(order);
        order.setTotalPrice(totalPrice);
        orderRepository.createOrder(order);

        return order.getTotalPrice();
    }

    /**
     * Updates an existing order and recalculates the total price based on modified product list.
     *
     * @param order Updated Order object.
     * @return Total price of the updated order.
     */
    @Override
    public int updateOrder(Order order) {
        int totalPrice = calculatePrice(order);
        order.setTotalPrice(totalPrice);
        orderRepository.updateOrder(order);

        return order.getTotalPrice();
    }

    /**
     * Deletes an order.
     *
     * @param order Order object to delete.
     */
    @Override
    public void deleteOrder(Order order) {
        orderRepository.deleteOrder(order);

    }

    /**
     * Calculates the total price of an order based on product prices and applicable offers.
     *
     * @param order Order object for which to calculate the price.
     * @return Total price of the order.
     */
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

    /**
     * Calculates the total price applying the "2 for 3" deal on the first three products.
     *
     * @param products List of products in the order.
     * @return Total discounted price based on the "2 for 3" offer.
     */
    public int calculateTwoForThreeDeal(List<Product> products) {
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

    /**
     * Calculates the total price applying the "Buy One, Get One Half Price" offer on remaining products.
     *
     * @param products List of products in the order (after the first three).
     * @return Total discounted price based on the "Buy One, Get One Half Price" offer.
     */
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
