package com.example.customer_api_service.controller;

import com.example.customer_api_service.model.Customer;
import com.example.customer_api_service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        logger.info("Received request to create customer: {}", customer.getEmail());
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        logger.info("Fetching all customers");
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable UUID id) {
        logger.info("Fetching customer by ID: {}", id);
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Customer not found for ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable UUID id, @RequestBody Customer updatedCustomer) {
        logger.info("Updating customer with ID: {}", id);
        return ResponseEntity.ok(customerService.updateCustomer(id, updatedCustomer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        logger.info("Deleting customer with ID: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
