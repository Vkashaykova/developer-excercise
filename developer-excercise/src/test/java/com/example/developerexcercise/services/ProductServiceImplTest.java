package com.example.developerexcercise.services;

import com.example.developerexcercise.exseptions.DuplicateEntityException;
import com.example.developerexcercise.exseptions.EntityNotFoundException;
import com.example.developerexcercise.models.Product;
import com.example.developerexcercise.repositories.contracts.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.developerexcercise.helpers.createMockProduct;
import static com.example.developerexcercise.helpers.createUpdatedMockProduct;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

public class ProductServiceImplTest {
    @Mock
    private ProductRepository mockRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getAllProducts_ShouldReturnListOfProducts() {
        // Arrange
        Product mockProduct = createMockProduct();
        List<Product> expectedProducts = Collections.singletonList(mockProduct);
        Mockito.when(mockRepository.getAllProducts()).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productService.getAllProducts();

        // Assert
        assertArrayEquals(expectedProducts.toArray(), actualProducts.toArray());
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() {
        //Arrange
        Product expectedProduct = createMockProduct();
        Mockito.when(mockRepository.getProductById(expectedProduct.getProductId())).
                thenReturn(Optional.of(expectedProduct));

        //Act
        Product actualProduct = productService.getProductById(expectedProduct.getProductId());

        // Assert
        Assertions.assertEquals(expectedProduct, actualProduct);
    }

    @Test
    void getProductById_ShouldThrowException_WhenProductDoesNotExist() {
        //Arrange
        int invalidId = 999;
        Mockito.when(mockRepository.getProductById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> productService.getProductById(invalidId));
    }

    @Test
    void getProductByName_ShouldReturnProduct_WhenProductExists() {
        //Arrange
        Product expectedProduct = createMockProduct();
        Mockito.when(mockRepository.getProductByName(expectedProduct.getProductName())).
                thenReturn(Optional.of(expectedProduct));

        //Act
        Optional<Product> actualProduct = productService.getProductByName(expectedProduct.getProductName());

        // Assert
        Assertions.assertTrue(actualProduct.isPresent(), "Product should be present");
        Assertions.assertEquals(expectedProduct, actualProduct.get(), "Products should be equal");
    }

    @Test
    void addProduct_ShouldAddProduct_WhenProductDoesNotExist() {
        //Arrange
        Product newProduct = createMockProduct();
        Mockito.when(mockRepository.getProductByName(newProduct.getProductName())).thenReturn(Optional.empty());

        //Act
        productService.addProduct(newProduct);

        // Assert
        Mockito.verify(mockRepository, times(1)).addProduct(newProduct);
    }

    @Test
    void addProduct_ShouldThrowException_WhenProductAlreadyExists() {
        // Arrange
        Product existingProduct = createMockProduct();
        Product newProduct = createMockProduct();

        Mockito.when(mockRepository.getProductByName(newProduct.getProductName()))
                .thenReturn(Optional.of(existingProduct));

        // Act & Assert
        assertThrows(DuplicateEntityException.class, () -> productService.addProduct(newProduct));
    }

    @Test
    void updateProduct_ShouldUpdateProduct_WhenProductExists() {
        // Arrange
        Product productToUpdate = createMockProduct();
        Product updatedProduct = createUpdatedMockProduct(productToUpdate); // Create an updated version of productToUpdate

        // Mock repository behavior
        Mockito.when(mockRepository.getProductById(productToUpdate.getProductId())).thenReturn(Optional.of(productToUpdate));
        Mockito.when(mockRepository.getProductByName(updatedProduct.getProductName())).thenReturn(Optional.empty()); // No product with the updated name and price

        // Act
        productService.updateProduct(updatedProduct);

        // Assert
        Mockito.verify(mockRepository, times(1)).updateProduct(updatedProduct);
    }

    @Test
    void updateProduct_ShouldThrowException_WhenProductHasSameNameAndPrice() {
        //Arrange
        Product mockProduct = createMockProduct();
        Product existingProduct = createMockProduct();

        existingProduct.setProductId(mockProduct.getProductId());
        existingProduct.setProductName(mockProduct.getProductName());
        existingProduct.setPrice(mockProduct.getPrice());

        Mockito.when(mockRepository.getProductById(mockProduct.getProductId())).thenReturn(Optional.of(existingProduct));

        // Act & Assert
        assertThrows(DuplicateEntityException.class, () -> productService.updateProduct(mockProduct));

    }

    @Test
    void deleteProduct_ShouldDeleteProduct() {
        // Arrange
        Product mockProduct = createMockProduct();

        // Act
        productService.deleteProduct(mockProduct);

        // Assert
        Mockito.verify(mockRepository, times(1)).
                deleteProduct(mockProduct);
    }
}

