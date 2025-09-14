package com.inkomoko.inkomoko.service;

import com.inkomoko.inkomoko.model.Customer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CustomerProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RetryTemplate retryTemplate;

    public CustomerProducer(KafkaTemplate<String, Object> kafkaTemplate, RetryTemplate retryTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.retryTemplate = retryTemplate;
    }

    @Async
    public void sendCustomer(Customer customer) {
        try {
            retryTemplate.execute(context -> {
                kafkaTemplate.send("customer_data", customer.getId(), customer).get(); // .get() waits for send result
                System.out.println("✅ Sent customer: " + customer.getName());
                return null;
            });
        } catch (Exception e) {
            System.err.println("❌ Failed to send customer after retries: " + customer.getName() + " -> " + e.getMessage());
        }
    }
}
