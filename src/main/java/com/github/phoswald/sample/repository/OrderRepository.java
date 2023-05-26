package com.github.phoswald.sample.repository;

import java.util.List;
import java.util.Optional;

import com.github.phoswald.sample.application.Order;

import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Dependent
public class OrderRepository {

    @PersistenceContext(name = "appDS")
    private EntityManager em;
    
    public List<Order> findOrders(Integer offset, Integer count) {
        TypedQuery<OrderEntity> query = em.createNamedQuery(OrderEntity.SELECT_ALL, OrderEntity.class);
        if(offset != null) {
            query.setFirstResult(offset.intValue());
        }
        if(count != null) {
            query.setMaxResults(count.intValue());
        }
        return query.getResultList().stream().map(this::mapOrderEntity).toList();
    }

    public Optional<Order> findOrderById(String orderId) {
        return Optional.ofNullable(em.find(OrderEntity.class, orderId)).map(this::mapOrderEntity);
    }

    public void storeOrder(Order order) {
        OrderEntity entity = mapOrder(order);
        if(em.find(OrderEntity.class, order.getOrderId()) == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    private Order mapOrderEntity(OrderEntity entity) {
        return Order.builder()
                .orderId(entity.getOrderId())
                .orderDetails(entity.getOrderDetails())
                .paymentId(entity.getPaymentId())
                .status(entity.getStatus())
                .timestamp(entity.getTimestamp())
                .build();
    }
    
    private OrderEntity mapOrder(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setOrderId(order.getOrderId());
        entity.setOrderDetails(order.getOrderDetails());
        entity.setPaymentId(order.getPaymentId());
        entity.setStatus(order.getStatus());
        entity.setTimestamp(order.getTimestamp());
        return entity;
    }
}
