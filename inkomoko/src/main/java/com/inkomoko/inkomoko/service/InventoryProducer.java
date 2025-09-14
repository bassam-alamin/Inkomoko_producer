package com.inkomoko.inkomoko.service;

import com.inkomoko.inkomoko.model.Product;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class InventoryProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RetryTemplate retryTemplate;

    public InventoryProducer(KafkaTemplate<String, Object> kafkaTemplate, RetryTemplate retryTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.retryTemplate = retryTemplate;
    }

    @Async
    public void sendProduct(Product product) {
        try {
            retryTemplate.execute(context -> {
                kafkaTemplate.send("inventory_data", product.getId(), product).get(); // Wait for send result
                System.out.println("✅ Sent product: " + product.getName());
                return null;
            });
        } catch (Exception e) {
            System.err.println("❌ Failed to send product after retries: " + product.getName() + " -> " + e.getMessage());
        }
    }
}
