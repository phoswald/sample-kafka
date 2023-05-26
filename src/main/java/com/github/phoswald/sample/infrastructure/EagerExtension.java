package com.github.phoswald.sample.infrastructure;

import com.github.phoswald.sample.adapter.consumer.EventConsumer;
import com.github.phoswald.sample.adapter.producer.EventProducer;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

public class EagerExtension implements Extension {

    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
        bbd.addAnnotatedType(EventProducer.class, EventProducer.class.getName());
        bbd.addAnnotatedType(EventConsumer.class, EventConsumer.class.getName());
    }
}
