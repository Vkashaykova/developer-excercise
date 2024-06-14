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

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;

    private final OrderMapper orderMapper;

    @Autowired
    public OrderRestController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
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
    public ResponseEntity<String> createOrderAndCalculatePrice(@RequestBody OrderDto orderDto) {

        try {
            Order newOrder = orderMapper.fromDto(orderDto);

            int totalPrice = orderService.createOrder(newOrder);
            int aws = totalPrice / 100;
            int clouds = totalPrice % 100;
            String formattedPrice = totalPrice + "clouds = " + aws + "." + String.format("%02d", clouds) + "aws";
            return new ResponseEntity<>(formattedPrice, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

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
