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

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public ProductRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Product> getAllProducts() {

        try (Session session = sessionFactory.openSession()) {
            Query<Product> query = session.createQuery
                    ("SELECT p from Product p", Product.class);

            return query.list();
        }
    }

    @Override
    public Optional<Product> getProductById(int productId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Product> query = session.createQuery
                    ("FROM Product as p where p.productId = :productId", Product.class);
            query.setParameter("productId", productId);

            return Optional.ofNullable(query.uniqueResult());
        }
    }

    @Override
    public Optional<Product> getProductByName(String productName) {

        try (Session session = sessionFactory.openSession()) {
            Query<Product> query = session.createQuery
                    ("select p FROM Product p where p.productName = :productName", Product.class);
            query.setParameter("productName", productName);

            return Optional.ofNullable(query.uniqueResult());
        }
    }

    @Override
    public void addProduct(Product product) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(product);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updateProduct(Product product) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(product);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteProduct(Product product) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(product);
            session.getTransaction().commit();
        }
    }
}