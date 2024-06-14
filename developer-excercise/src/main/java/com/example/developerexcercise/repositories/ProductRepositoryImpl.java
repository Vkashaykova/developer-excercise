package com.example.developerexcercise.repositories;

import com.example.developerexcercise.models.Product;
import com.example.developerexcercise.repositories.contracts.ProductRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ProductRepository interface using Hibernate.
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final SessionFactory sessionFactory;

    /**
     * Constructs an instance of ProductRepositoryImpl with dependencies injected.
     *
     * @param sessionFactory The Hibernate session factory.
     */
    @Autowired
    public ProductRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return A list of all Product objects.
     */
    @Override
    public List<Product> getAllProducts() {

        try (Session session = sessionFactory.openSession()) {
            Query<Product> query = session.createQuery
                    ("SELECT p from Product p", Product.class);

            return query.list();
        }
    }

    /**
     * Retrieves a product by its ID from the database.
     *
     * @param productId The ID of the product to retrieve.
     * @return An Optional containing the Product object if found, or empty if not found.
     */
    @Override
    public Optional<Product> getProductById(int productId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Product> query = session.createQuery
                    ("FROM Product as p where p.productId = :productId", Product.class);
            query.setParameter("productId", productId);

            return Optional.ofNullable(query.uniqueResult());
        }
    }

    /**
     * Retrieves a product by its name from the database.
     *
     * @param productName The name of the product to retrieve.
     * @return An Optional containing the Product object if found, or empty if not found.
     */
    @Override
    public Optional<Product> getProductByName(String productName) {

        try (Session session = sessionFactory.openSession()) {
            Query<Product> query = session.createQuery
                    ("select p FROM Product p where p.productName = :productName", Product.class);
            query.setParameter("productName", productName);

            return Optional.ofNullable(query.uniqueResult());
        }
    }

    /**
     * Adds a new product to the database.
     *
     * @param product The Product object to add.
     */
    @Override
    public void addProduct(Product product) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(product);
            session.getTransaction().commit();
        }
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product The Product object containing updated details.
     */
    @Override
    public void updateProduct(Product product) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(product);
            session.getTransaction().commit();
        }
    }

    /**
     * Deletes a product from the database.
     *
     * @param product The Product object to delete.
     */
    @Override
    public void deleteProduct(Product product) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(product);
            session.getTransaction().commit();
        }
    }
}