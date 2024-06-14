package com.example.developerexcercise.controllers.rest;

import com.example.developerexcercise.helpers.OrderMapper;
import com.example.developerexcercise.models.Order;
import com.example.developerexcercise.models.dtos.OrderDto;
import com.example.developerexcercise.services.contracts.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * RestController handling CRUD operations for orders via REST API endpoints.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    /**
     * Constructs an instance of OrderRestController with dependencies injected.
     *
     * @param orderService The service handling order operations.
     * @param orderMapper  The mapper converting between OrderDto and Order entities.
     */
    @Autowired
    public OrderRestController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    /**
     * Retrieves all orders.
     *
     * @return ResponseEntity containing a list of Order objects.
     * @throws ResponseStatusException if no orders are found.
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {

        try {
            List<Order> orders = orderService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId ID of the order to retrieve.
     * @return ResponseEntity containing the requested Order object.
     * @throws ResponseStatusException if the order with the given ID is not found.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable int orderId) {

        try {
            Order order = orderService.getOrderById(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Fetches all orders that contain a specific product.
     *
     * @param productName Name of the product to filter orders by.
     * @return ResponseEntity with a list of Order entities and HTTP status OK (200).
     * @throws EntityNotFoundException if no orders are found for the given product.
     */
    @GetMapping("/product/{productName}")
    public ResponseEntity<List<Order>> getOrdersByProduct(@PathVariable String productName) {
        try {
            List<Order> orders = orderService.getOrdersByProduct(productName);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    /**
     * Creates a new order from the provided OrderDto and calculates the total price.
     *
     * @param orderDto DTO object containing order details.
     * @return ResponseEntity with a formatted string indicating the total price.
     * @throws ResponseStatusException if a referenced entity (product) is not found.
     */
    @PostMapping
    public ResponseEntity<String> createOrderAndCalculatePrice(@RequestBody OrderDto orderDto) {

        try {
            Order newOrder = orderMapper.fromDto(orderDto);

            int totalPrice = orderService.createOrder(newOrder);
            int aws = totalPrice / 100;
            int clouds = totalPrice % 100;
            String formattedPrice = totalPrice + " clouds = " + aws + "." + String.format("%02d", clouds) + " aws";
            return new ResponseEntity<>(formattedPrice, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Updates an existing order identified by its ID using data from the provided OrderDto.
     *
     * @param orderId  ID of the order to update.
     * @param orderDto DTO object containing updated order details.
     * @return ResponseEntity with a formatted string indicating the updated total price.
     * @throws ResponseStatusException if the order with the given ID or a referenced entity (product) is not found.
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<String> updateOrder(@PathVariable int orderId,
                                              @RequestBody OrderDto orderDto) {
        try {
            orderService.getOrderById(orderId);
            Order updateOrder = orderMapper.fromDto(orderId, orderDto);
            int totalPrice = orderService.updateOrder(updateOrder);
            int aws = totalPrice / 100;
            int clouds = totalPrice % 100;
            String formattedPrice = totalPrice + "clouds = " + aws + "." + String.format("%02d", clouds) + "aws";
            return new ResponseEntity<>(formattedPrice, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Deletes an order identified by its ID.
     *
     * @param orderId ID of the order to delete.
     * @return ResponseEntity containing the deleted Order object.
     * @throws ResponseStatusException if the order with the given ID is not found.
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Order> deleteOrder(@PathVariable int orderId) {

        try {
            Order deleteOrder = orderService.getOrderById(orderId);
            orderService.deleteOrder(deleteOrder);
            return new ResponseEntity<>(deleteOrder, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
