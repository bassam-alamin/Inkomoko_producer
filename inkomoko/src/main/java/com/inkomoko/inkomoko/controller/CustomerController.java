package com.inkomoko.inkomoko.controller;

import com.inkomoko.inkomoko.model.Customer;
import com.inkomoko.inkomoko.service.CustomerProducer;
import com.inkomoko.inkomoko.service.CustomerCache;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerProducer customerProducer;
    private final CustomerCache customerCache; // inject cache

    public CustomerController(CustomerProducer customerProducer, CustomerCache customerCache) {
        this.customerProducer = customerProducer;
        this.customerCache = customerCache;
    }

    @Operation(
            summary = "Produce a customer record",
            description = "Accepts a JSON customer object and sends it to the `customer_data` Kafka topic.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer produced successfully")
            }
    )
    @PostMapping
    public String addCustomer(@org.springframework.web.bind.annotation.RequestBody Customer customer) {
        customerProducer.sendCustomer(customer);
        return "✅ Customer produced: " + customer.getName();
    }

    @Operation(
            summary = "Get all customers from Kafka cache",
            description = "Returns a list of customers that were consumed from Kafka (pub/sub cache).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of customers returned")
            }
    )
    @GetMapping
    public List<Customer> getCustomers() {
        // fetch customers from in-memory cache populated by Kafka consumer
        return customerCache.getAllCustomers();
    }
}
