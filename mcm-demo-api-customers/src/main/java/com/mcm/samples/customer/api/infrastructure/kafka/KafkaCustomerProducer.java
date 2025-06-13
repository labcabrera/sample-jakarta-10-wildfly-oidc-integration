package com.mcm.samples.customer.api.infrastructure.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.mcm.samples.customer.api.application.service.CustomerMessageProducer;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class KafkaCustomerProducer implements CustomerMessageProducer {

    private Producer<String, String> producer;

    public KafkaCustomerProducer() {
        try {
            String kafkaBroker = System.getenv("KAFKA_BROKER");
            if (kafkaBroker == null || kafkaBroker.isEmpty()) {
                throw new RuntimeException("Required environment variable KAFKA_BROKER is not set");
            }
            log.info("Creating Kafka producer with broker: {}", kafkaBroker);
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBroker);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            props.put(ProducerConfig.ACKS_CONFIG, "all");
            producer = new KafkaProducer<>(props);
        }
        catch (Exception ex) {
            log.error("Error creating kafka producer", ex);
        }
    }

    @Override
    public void sendCreatedCustomerEvent(String userId, String email) {
        if (producer == null) {
            log.error("Kafka producer is not initialized");
            return;
        }
        //TODO read from configuration
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>("created-users-topic", userId, email);
            producer.send(record);
        }
        catch (Exception ex) {
            log.error("Error sending customer created event to Kafka", ex);
        }
    }
}
