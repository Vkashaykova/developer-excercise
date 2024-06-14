package com.example.developerexcercise.repositories;

import com.example.developerexcercise.models.Order;
import com.example.developerexcercise.repositories.contracts.OrderRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of OrderRepository providing CRUD operations for Order entities using Hibernate.
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final SessionFactory sessionFactory;

    /**
     * Constructs an instance of OrderRepositoryImpl with a Hibernate SessionFactory.
     *
     * @param sessionFactory Hibernate SessionFactory to create sessions.
     */
    @Autowired
    public OrderRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Retrieves all orders from the database.
     *
     * @return List of all Order entities.
     */
    @Override
    public List<Order> getAllOrders() {

        try (Session session = sessionFactory.openSession()) {
            Query<Order> query = session.createQuery
                    ("SELECT o from Order o", Order.class);
            return query.list();
        }
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId ID of the order to retrieve.
     * @return Optional containing the Order entity if found, otherwise empty.
     */
    @Override
    public Optional<Order> getOrderById(int orderId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Order> query = session.createQuery
                    ("FROM Order as o where o.orderId = :orderId", Order.class);
            query.setParameter("orderId", orderId);

            return Optional.ofNullable(query.uniqueResult());
        }
    }

    /**
     * Retrieves orders containing a specific product.
     *
     * @param productId ID of the product to filter orders by.
     * @return Optional containing a List of Order entities if found, otherwise empty.
     */
    @Override
    public Optional<List<Order>> getOrdersByProduct(int productId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Order> query = session.createQuery
                    ("select o FROM Order o JOIN o.products p where p.productId = :productId");
            query.setParameter("productId", productId);
            List<Order> result = query.list();

            return Optional.ofNullable(result.isEmpty() ? null : result);
        }
    }

    /**
     * Creates a new order in the database.
     *
     * @param order Order entity to create.
     */
    @Override
    public void createOrder(Order order) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(order);
            session.getTransaction().commit();
        }
    }

    /**
     * Updates an existing order in the database.
     *
     * @param order Updated Order entity.
     */
    @Override
    public void updateOrder(Order order) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(order);
            session.getTransaction().commit();
        }
    }

    /**
     * Deletes an existing order from the database.
     *
     * @param order Order entity to delete.
     */
    @Override
    public void deleteOrder(Order order) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(order);
            session.getTransaction().commit();
        }

    }
}
