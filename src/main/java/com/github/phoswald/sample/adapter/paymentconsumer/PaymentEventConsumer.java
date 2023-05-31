package com.github.phoswald.sample.adapter.paymentconsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.phoswald.sample.application.OrderApplication;
import com.github.phoswald.sample.application.PaymentEvent;

import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

@ApplicationScoped
public class PaymentEventConsumer {

    private static final Jsonb json = JsonbBuilder.create();
    
    private final Logger logger = LoggerFactory.getLogger(PaymentEventConsumer.class);
    
    @Inject
    @ConfigProperty(name="app.kafka.bootstrap.servers")
    private String kafkaBootstrapServers;
    
    @Inject
    @ConfigProperty(name="app.kafka.consumer.topic")
    private String kafkaTopic;
    
    @Resource(lookup = "concurrent/appMES")
    private ManagedExecutorService executorService;
    
    @Inject
    private OrderApplication application;
    
    private volatile KafkaConsumer<String, String> kafkaConsumer; 
    
    void open(@Observes @Initialized(ApplicationScoped.class) Object event) {
        logger.info("Opening...");
        kafkaConsumer = new KafkaConsumer<>(getProperties());
        kafkaConsumer.subscribe(Collections.singleton(kafkaTopic));
        executorService.submit(this::processMessages);
        logger.info("Opening successful.");
    }
    
    // TODO: not called reliably, and not waited for completion
    void close(@Observes @Destroyed(ApplicationScoped.class) Object event) {
        logger.info("Closing...");
        kafkaConsumer.close();
        kafkaConsumer = null;
        logger.info("Closing successful.");
    }

    private void processMessages() {
        logger.info("Processing...");
        while(kafkaConsumer != null) {
            kafkaConsumer.poll(Duration.ofMillis(100)).forEach(this::processEvent);
        }
        logger.info("Processing finished.");
    }
    
    private void processEvent(ConsumerRecord<String, String> event) {
        try {
            logger.info("Consuming topic={}, offset={}, key={}, value={}",
                    kafkaTopic, event.offset(), event.key(), event.value());
            application.handlePaymentEvent(deserializeEvent(event.value()));
            
        } catch(RuntimeException e) {
            logger.error("Error while processing Kafka event:", e);
        }
    }
    
    private PaymentEvent deserializeEvent(String value) {
        return json.fromJson(value, PaymentEvent.class);
    }

    private Properties getProperties() {
        var props = new Properties();
        props.put("bootstrap.servers", kafkaBootstrapServers);
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }
}
