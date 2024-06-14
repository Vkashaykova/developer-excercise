package com.example.developerexcercise.controllers.rest;

import com.example.developerexcercise.exseptions.DuplicateEntityException;
import com.example.developerexcercise.helpers.ProductMapper;
import com.example.developerexcercise.models.Product;
import com.example.developerexcercise.models.dtos.ProductDto;
import com.example.developerexcercise.services.contracts.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST controller for managing products.
 */
@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    /**
     * Constructs an instance of ProductRestController with dependencies injected.
     *
     * @param productService The service handling product operations.
     * @param productMapper  The mapper converting between ProductDto and Product entities.
     */
    @Autowired
    public ProductRestController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    /**
     * Retrieves all products.
     *
     * @return ResponseEntity containing a list of Product objects.
     * @throws ResponseStatusException if no products are found.
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {

        try {
            List<Product> products = productService.getAllProducts();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId ID of the product to retrieve.
     * @return ResponseEntity containing the requested Product object.
     * @throws ResponseStatusException if the product with the given ID is not found.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable int productId) {

        try {
            Product product = productService.getProductById(productId);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Retrieves a product by its name.
     *
     * @param productName Name of the product to retrieve.
     * @return ResponseEntity containing the requested Product object.
     * @throws ResponseStatusException if the product with the given name is not found.
     */
    @GetMapping("/name/{productName}")
    public ResponseEntity<Product> getProductByName(@PathVariable String productName) {

        try {
            Product product = productService.getProductByName(productName).get();
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Creates a new product from the provided ProductDto.
     *
     * @param productDto DTO object containing product details.
     * @return ResponseEntity containing the created Product object.
     * @throws ResponseStatusException if a referenced entity (product) is not found or a duplicate product is found.
     */
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductDto productDto) {
        try {
            Product product = productMapper.fromDto(productDto);
            productService.addProduct(product);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    /**
     * Updates an existing product identified by its ID using data from the provided ProductDto.
     *
     * @param productId  ID of the product to update.
     * @param productDto DTO object containing updated product details.
     * @return ResponseEntity containing the updated Product object.
     * @throws ResponseStatusException if the product with the given ID or a referenced entity (product) is not found, or a duplicate product is found.
     */
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable int productId,
                                                 @RequestBody ProductDto productDto) {
        try {
            productService.getProductById(productId);
            Product product = productMapper.fromDto(productId, productDto);
            productService.updateProduct(product);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    /**
     * Deletes a product identified by its ID.
     *
     * @param productId ID of the product to delete.
     * @return ResponseEntity containing the deleted Product object.
     * @throws ResponseStatusException if the product with the given ID or a referenced entity (product) is not found, or a duplicate product is found.
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable int productId) {

        try {
            Product deleteProduct = productService.getProductById(productId);
            productService.deleteProduct(deleteProduct);
            return new ResponseEntity<>(deleteProduct, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}


