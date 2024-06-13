package com.example.developerexcercise.repositories;

import com.example.developerexcercise.models.Order;
import com.example.developerexcercise.repositories.contracts.OrderRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public OrderRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Order> getAllOrders() {
        return null;
    }

    @Override
    public Order getOrderById(int orderId) {
        return null;
    }

    @Override
    public List<Order> getOrdersByProduct(int productId) {
        return null;
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
