package com.inkomoko.inkomoko.controller;

import com.inkomoko.inkomoko.client.CrmClient;
import com.inkomoko.inkomoko.client.InventoryClient;
import com.inkomoko.inkomoko.service.CustomerProducer;
import com.inkomoko.inkomoko.service.InventoryProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private final CrmClient crmClient;
    private final InventoryClient inventoryClient;
    private final CustomerProducer customerProducer;
    private final InventoryProducer inventoryProducer;

    public AdminController(CrmClient crmClient,
                           InventoryClient inventoryClient,
                           CustomerProducer customerProducer,
                           InventoryProducer inventoryProducer) {
        this.crmClient = crmClient;
        this.inventoryClient = inventoryClient;
        this.customerProducer = customerProducer;
        this.inventoryProducer = inventoryProducer;
    }

    @Operation(
            summary = "Send customer records to Kafka",
            description = "Fetches customer data from the CRM mock API and produces it into the `customer_data` Kafka topic.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customers sent successfully",
                            content = @Content(
                                    examples = @ExampleObject(value = "✅ Customers sent to Kafka!")
                            )
                    )
            }
    )
    @GetMapping("/admin/produce/customers")
    public String produceCustomers() {
        crmClient.fetchCustomers().forEach(customerProducer::sendCustomer);
        return "✅ Customers sent to Kafka!";
    }

    @Operation(
            summary = "Send inventory records to Kafka",
            description = "Fetches inventory data from the Inventory mock API and produces it into the `inventory_data` Kafka topic.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Inventory sent successfully",
                            content = @Content(
                                    examples = @ExampleObject(value = "✅ Inventory sent to Kafka!")
                            )
                    )
            }
    )
    @GetMapping("/admin/produce/inventory")
    public String produceInventory() {
        inventoryClient.fetchProducts().forEach(inventoryProducer::sendProduct);
        return "✅ Inventory sent to Kafka!";
    }
}
