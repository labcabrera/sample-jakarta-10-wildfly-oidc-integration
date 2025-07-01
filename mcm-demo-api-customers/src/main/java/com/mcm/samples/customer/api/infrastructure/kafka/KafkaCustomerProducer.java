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

    private static final String APP_SASL_TEMPLATE = "org.apache.kafka.common.security.plain.PlainLoginModule required\nusername=\"%s\"\npassword=\"%s\";";

    private Producer<String, String> producer;

    private String topicName;

    public KafkaCustomerProducer() {
        try {
            String kafkaBroker = System.getenv("APP_KAFKA_BROKER");
            topicName = System.getenv("APP_KAFKA_TOPIC_CUSTOMER_CREATED");
            if (kafkaBroker == null || kafkaBroker.isEmpty()) {
                return;
            }
            log.info("Creating Kafka producer with broker: {}", kafkaBroker);
            Properties props = readKafkaProperties(kafkaBroker);
            producer = new KafkaProducer<>(props);
        }
        catch (Exception ex) {
            log.error("Error creating Kafka producer", ex);
        }
    }

    @Override
    public void sendCreatedCustomerEvent(String userId, String email) {
        if (producer == null) {
            log.error("Kafka producer is not initialized");
            return;
        }
        try {
            String message = String.format("{\"customerId\":\"%s\", \"email\":\"%s\"}", userId, email);
            ProducerRecord<String, String> record = new ProducerRecord<>(topicName, userId, message);
            producer.send(record);
        }
        catch (Exception ex) {
            log.error("Error sending customer created event to Kafka", ex);
        }
    }

    private Properties readKafkaProperties(String kafkaBroker) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBroker);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        String username = System.getenv("APP_KAFKA_USERNAME");
        String password = System.getenv("APP_KAFKA_PASSWORD");
        if (username != null && password != null) {
            String saslJaasConfig = String.format(APP_SASL_TEMPLATE, username, password);
            props.put("security.protocol", "SASL_PLAINTEXT");
            props.put("sasl.mechanism", "PLAIN");
            props.put("sasl.jaas.config", saslJaasConfig);
        }
        return props;
    }
}
