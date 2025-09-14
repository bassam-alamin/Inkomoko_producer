package com.inkomoko.inkomoko.service;

import com.inkomoko.inkomoko.model.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CustomerCache {

    private final Map<String, Customer> customerMap = new ConcurrentHashMap<>();

    public void addOrUpdateCustomer(Customer customer) {
        customerMap.put(customer.getId(), customer);
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerMap.values());
    }
}
