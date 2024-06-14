package com.example.developerexcercise.services;

import com.example.developerexcercise.exseptions.EntityNotFoundException;
import com.example.developerexcercise.models.Order;
import com.example.developerexcercise.models.Product;
import com.example.developerexcercise.repositories.contracts.OrderRepository;
import com.example.developerexcercise.services.contracts.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.developerexcercise.helpers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository mockOrderRepository;

    @Mock
    private ProductService mockProductService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() {
        // Arrange
        Order mockOrder = createMockOrder();
        List<Order> expectedOrders = Collections.singletonList(mockOrder);
        when(mockOrderRepository.getAllOrders()).thenReturn(expectedOrders);

        // Act
        List<Order> actualOrders = orderService.getAllOrders();

        // Assert
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void getOrderById_ShouldReturnOrder_WhenOrderExists() {
        // Arrange
        Order expectedOrder = createMockOrder();
        when(mockOrderRepository.getOrderById(expectedOrder.getOrderId())).thenReturn(java.util.Optional.of(expectedOrder));

        // Act
        Order actualOrder = orderService.getOrderById(expectedOrder.getOrderId());

        // Assert
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void getOrderById_ShouldThrowException_WhenOrderDoesNotExist() {
        // Arrange
        int invalidId = 999;
        when(mockOrderRepository.getOrderById(invalidId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> orderService.getOrderById(invalidId));
    }

    @Test
    void getOrdersByProduct_ShouldReturnListOfOrders_WhenProductExistsInOrders() {
        // Arrange
        String productName = "Mock Product";
        Product mockProduct = createMockProduct();
        Order mockOrder = createMockOrder();
        when(mockProductService.getProductByName(productName)).thenReturn(java.util.Optional.of(mockProduct));
        when(mockOrderRepository.getOrdersByProduct(mockProduct.getProductId())).thenReturn(java.util.Optional.of(Collections.singletonList(mockOrder)));

        // Act
        List<Order> actualOrders = orderService.getOrdersByProduct(productName);

        // Assert
        assertEquals(1, actualOrders.size());
        assertEquals(mockOrder, actualOrders.get(0));
    }

    @Test
    void getOrdersByProduct_ShouldThrowException_WhenProductDoesNotExistInOrders() {
        // Arrange
        int invalidId = 999;
        Mockito.when(mockOrderRepository.getOrderById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> orderService.getOrderById(invalidId));
    }

    @Test
    void createOrder_ShouldCreateOrderWithTotalPrice() {
        // Arrange
        Order mockOrder = createMockOrder();
        //when(mockOrderRepository.createOrder(any(Order.class))).thenReturn(mockOrder.getOrderId());
        when(mockProductService.getProductByName(anyString())).thenReturn(java.util.Optional.of(createMockProduct()));

        // Act
        int totalPrice = orderService.createOrder(mockOrder);

        // Assert
        assertEquals(mockOrder.getTotalPrice(), totalPrice);
        Mockito.verify(mockOrderRepository, times(1)).createOrder(mockOrder);
    }

    @Test
    void updateOrder_ShouldUpdateOrderWithTotalPrice() {
        // Arrange
        Order mockOrder = createMockOrder();
        //when(mockOrderRepository.updateOrder(any(Order.class))).thenReturn(mockOrder.getOrderId());
        when(mockProductService.getProductByName(anyString())).thenReturn(java.util.Optional.of(createMockProduct()));

        // Act
        int totalPrice = orderService.updateOrder(mockOrder);

        // Assert
        assertEquals(mockOrder.getTotalPrice(), totalPrice);
        Mockito.verify(mockOrderRepository, times(1)).updateOrder(mockOrder);
    }

    @Test
    void deleteOrder_ShouldDeleteOrder() {
        // Arrange
        Order mockOrder = createMockOrder();

        // Act
        orderService.deleteOrder(mockOrder);

        // Assert
        Mockito.verify(mockOrderRepository, times(1)).deleteOrder(mockOrder);
    }

    @Test
    void testCalculateTwoForThreeDeal() {
        Product product1 = createMockProduct();
        Product product2 = createMockProduct2();
        Product product3 = createMockProduct3();


        List<Product> products = Arrays.asList(product1, product2, product3);

        int totalPrice = orderService.calculateTwoForThreeDeal(products);

        assertEquals(175, totalPrice);
    }
}
