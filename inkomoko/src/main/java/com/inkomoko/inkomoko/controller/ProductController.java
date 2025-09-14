package com.inkomoko.inkomoko.controller;

import com.inkomoko.inkomoko.model.Product;
import com.inkomoko.inkomoko.service.InventoryProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final InventoryProducer inventoryProducer;

    public ProductController(InventoryProducer inventoryProducer) {
        this.inventoryProducer = inventoryProducer;
    }

    @Operation(
            summary = "Produce a product record",
            description = "Accepts a JSON product object and sends it to the `inventory_data` Kafka topic.",
            requestBody = @RequestBody(
                    description = "Product JSON",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{ \"id\": \"101\", \"name\": \"Laptop\", \"quantity\": 50 }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product produced successfully")
            }
    )
    @PostMapping
    public String addProduct(@org.springframework.web.bind.annotation.RequestBody Product product) {
        inventoryProducer.sendProduct(product);
        return "✅ Product produced: " + product.getName();
    }
}
