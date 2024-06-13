package com.example.developerexcercise.controllers.rest;

import com.example.developerexcercise.helpers.OrderMapper;
import com.example.developerexcercise.models.Order;
import com.example.developerexcercise.models.dtos.OrderDto;
import com.example.developerexcercise.services.contracts.OrderService;
import com.example.developerexcercise.services.contracts.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;
    private final ProductService productService;

    private final OrderMapper orderMapper;

    @Autowired
    public OrderRestController(OrderService orderService, ProductService productService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.productService = productService;
        this.orderMapper = orderMapper;
    }
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {

        try {
            List<Order> orders = orderService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable int orderId) {

        try {
            Order order = orderService.getOrderById(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto orderDto) {

        try {
            Order newOrder = orderMapper.fromDto(orderDto);
            orderService.createOrder(newOrder);
            return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    }
