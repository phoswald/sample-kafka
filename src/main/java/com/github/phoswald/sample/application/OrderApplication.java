package com.github.phoswald.sample.application;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.phoswald.sample.adapter.producer.EventProducer;
import com.github.phoswald.sample.adapter.rest.OrderResource;
import com.github.phoswald.sample.repository.OrderRepository;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Dependent
@Transactional
public class OrderApplication {
    
    private final Logger logger = LoggerFactory.getLogger(OrderResource.class);
    
    @Inject
    private OrderRepository repository;
    
    @Inject
    private EventProducer eventProducer;
    
    public List<Order> findOrders(Integer offset, Integer count) {
        logger.info("Finding orders: offset={}, count={}", offset, count);
        return repository.findOrders(offset, count);
    }

    public Optional<Order> findOrderById(String orderId) {
        logger.info("Finding order: orderId={}", orderId);
        return repository.findOrderById(orderId);
    }
    
    public void addOrder(String orderId, String orderDetails, String paymentId) {
        Order order = Order.builder()
                .orderId(orderId)
                .orderDetails(orderDetails)
                .paymentId(paymentId)
                .status(OrderStatus.ORDERED)
                .timestamp(Instant.now())
                .build();
        logger.info("Adding order: {}", order);
        repository.storeOrder(order);
    }
    
    public void produceOrder(String orderId) {
        Optional<Order> order = repository.findOrderById(orderId);
        if(order.isPresent()) {
            logger.info("Producing: orderId={}", orderId);
            eventProducer.produceOrderEvent(order.get());
        } else {
            logger.warn("Producing not possible: orderId={} not found", orderId);
        }
    }
    
    public void consumePayment(String paymentId) {
        logger.info("Consuming: paymentId={}", paymentId);
    }
}
