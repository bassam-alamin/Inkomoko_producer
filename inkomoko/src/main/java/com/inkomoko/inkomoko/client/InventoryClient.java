package com.inkomoko.inkomoko.client;

import com.inkomoko.inkomoko.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryClient {

    // Simulating an Inventory REST call
    public List<Product> fetchProducts() {
        return List.of(
                new Product("P1", "Laptop", 50),
                new Product("P2", "Phone", 100)
        );
    }
}
