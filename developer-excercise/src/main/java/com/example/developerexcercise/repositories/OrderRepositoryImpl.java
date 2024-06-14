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

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public OrderRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Order> getAllOrders() {

        try (Session session = sessionFactory.openSession()) {
            Query<Order> query = session.createQuery
                    ("SELECT o from Order o", Order.class);
            return query.list();
        }
    }

    @Override
    public Optional<Order> getOrderById(int orderId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Order> query = session.createQuery
                    ("FROM Order as o where o.orderId = :orderId", Order.class);
            query.setParameter("orderId", orderId);

            return Optional.ofNullable(query.uniqueResult());
        }
    }

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

    @Override
    public void createOrder(Order order) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(order);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updateOrder(Order order) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(order);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteOrder(Order order) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(order);
            session.getTransaction().commit();
        }

    }
}
