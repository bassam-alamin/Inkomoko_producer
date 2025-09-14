package com.inkomoko.inkomoko.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inkomoko.inkomoko.model.Customer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

@Service
public class KafkaCustomerConsumer {

    private final CustomerCache customerCache;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaCustomerConsumer(CustomerCache customerCache) {
        this.customerCache = customerCache;
    }

    @PostConstruct
    public void startConsumer() {
        Thread thread = new Thread(this::consume);
        thread.setDaemon(true);
        thread.start();
    }

    private void consume() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "springboot-customer-cache");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList("customer_data"));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    try {
                        Map<String, Object> data = objectMapper.readValue(record.value(), Map.class);
                        Customer customer = new Customer();
                        customer.setId((String) data.get("id"));
                        customer.setName((String) data.get("name"));
                        customer.setEmail((String) data.get("email"));

                        customerCache.addOrUpdateCustomer(customer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
