package com.inkomoko.inkomoko.client;

import com.inkomoko.inkomoko.model.Customer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CrmClient {

    // Simulating a CRM REST call
    public List<Customer> fetchCustomers() {
        return List.of(
                new Customer("1", "Bassam", "Bassam@gmail.com"),
                new Customer("2", "alamin", "alamin@gmail.com")
        );
    }
}
