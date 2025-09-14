package com.inkomoko.inkomoko.service;

import com.inkomoko.inkomoko.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerProducerTest {

    private KafkaTemplate<String, Object> kafkaTemplate;
    private RetryTemplate retryTemplate;
    private CustomerProducer producer;

    @BeforeEach
    void setup() {
        kafkaTemplate = mock(KafkaTemplate.class);
        retryTemplate = mock(RetryTemplate.class);
        producer = new CustomerProducer(kafkaTemplate, retryTemplate);
    }

    @Test
    void testSendCustomerSuccess() throws Exception {
        Customer customer = new Customer();
        customer.setId("1");
        customer.setName("Bassam");

        // Mock retryTemplate to execute the callback
        when(retryTemplate.execute(any())).thenAnswer(invocation -> {
            invocation.getArgument(0, java.util.concurrent.Callable.class).call();
            return null;
        });

        producer.sendCustomer(customer);

        // Capture argument passed to KafkaTemplate
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(kafkaTemplate, times(1)).send(eq("customer_data"), eq("1"), captor.capture());

        Customer sent = (Customer) captor.getValue();
        assertEquals("Bassam", sent.getName());
    }

    @Test
    void testSendCustomerFailure() throws Exception {
        Customer customer = new Customer();
        customer.setId("2");
        customer.setName("Bassam");

        when(retryTemplate.execute(any())).thenThrow(new RuntimeException("Kafka down"));

        producer.sendCustomer(customer);

        // Verify KafkaTemplate was invoked
        verify(kafkaTemplate, times(1)).send(eq("customer_data"), eq("2"), any());
    }
}
