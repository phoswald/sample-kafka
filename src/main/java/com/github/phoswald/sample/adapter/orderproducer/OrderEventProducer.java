package com.github.phoswald.sample.adapter.orderproducer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.phoswald.sample.application.OrderEvent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

@ApplicationScoped
public class OrderEventProducer {

    private static final Jsonb json = JsonbBuilder.create();
    
    private final Logger logger = LoggerFactory.getLogger(OrderEventProducer.class);
    
    @Inject
    @ConfigProperty(name="app.kafka.bootstrap.servers")
    private String kafkaBootstrapServers;
    
    @Inject
    @ConfigProperty(name="app.kafka.producer.topic")
    private String kafkaTopic;
    
    private KafkaProducer<String, String> kafkaProducer; 
    
    void open(@Observes @Initialized(ApplicationScoped.class) Object event) {
        logger.info("Opening...");
        kafkaProducer = new KafkaProducer<>(getProperties());
        logger.info("Opening successful.");
    }
    
    // TODO: not called reliably
    void close(@Observes @Destroyed(ApplicationScoped.class) Object event) {
        logger.info("Closing...");
        kafkaProducer.close();
        kafkaProducer = null;
        logger.info("Closing successful.");
    }
    
    public void handleOrderEvent(OrderEvent event) {
        String value = serializeEvent(event);
        logger.info("Producing topic={}, value={}", kafkaTopic, event);
        kafkaProducer.send(new ProducerRecord<String, String>(kafkaTopic, value));
    }
    
    private String serializeEvent(OrderEvent event) {
        return json.toJson(event);
    }

    private Properties getProperties() {
        var props = new Properties();
        props.put("bootstrap.servers", kafkaBootstrapServers);
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }
}
