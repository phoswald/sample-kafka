package com.github.phoswald.sample.application;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.phoswald.sample.adapter.orderproducer.OrderEventProducer;
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
    private OrderEventProducer eventProducer;
    
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
   
    public void handlePaymentEvent(PaymentEvent paymentEvent) {
        logger.info("Consuming: paymentId={}", paymentEvent.getPaymentId());
        Optional<Order> orderOptional = repository.findOrderByPaymentId(paymentEvent.getPaymentId());
        if(orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(OrderStatus.PAYED);
            order.setTimestamp(Instant.now());
            repository.storeOrder(order);
            logger.info("Producing: order: {}", order);
            OrderEvent orderEvent = OrderEvent.builder()
                    .orderId(order.getOrderId())
                    .orderDetails(order.getOrderDetails())
                    .status(order.getStatus())
                    .build();
            eventProducer.handleOrderEvent(orderEvent);
        } else {
            logger.warn("Consuming not possible: paymentId={} not found", paymentEvent.getPaymentId());
        }
    }
}
