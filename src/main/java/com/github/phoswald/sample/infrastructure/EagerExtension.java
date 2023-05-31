package com.github.phoswald.sample.infrastructure;

import com.github.phoswald.sample.adapter.orderproducer.OrderEventProducer;
import com.github.phoswald.sample.adapter.paymentconsumer.PaymentEventConsumer;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

public class EagerExtension implements Extension {

    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
        bbd.addAnnotatedType(OrderEventProducer.class, OrderEventProducer.class.getName());
        bbd.addAnnotatedType(PaymentEventConsumer.class, PaymentEventConsumer.class.getName());
    }
}
