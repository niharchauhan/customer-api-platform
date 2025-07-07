package com.example.customer_api_service.service;

import com.example.customer_api_service.exception.DuplicateEmailException;
import com.example.customer_api_service.model.Customer;
import com.example.customer_api_service.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository repository;

    public Customer createCustomer(Customer customer) {
        logger.debug("Saving customer to database: {}", customer.getEmail());
        if (repository.findByEmail(customer.getEmail()).isPresent()) {
            logger.warn("Duplicate email found: {}", customer.getEmail());
            throw new DuplicateEmailException("A customer with this email already exists.");
        }
        return repository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        logger.debug("Retrieving all customers");
        return repository.findAll();
    }

    public Optional<Customer> getCustomerById(UUID id) {
        logger.debug("Looking up customer by ID: {}", id);
        return repository.findById(id);
    }

    public Customer updateCustomer(UUID id, Customer updatedCustomer) {
        logger.debug("Updating customer: {}", id);

        Optional<Customer> existingCustomerOpt = repository.findById(id);

        if (existingCustomerOpt.isEmpty()) {
            logger.error("Customer not found with ID: {}", id);
            throw new RuntimeException("Customer not found");
        }

        Customer existingCustomer = existingCustomerOpt.get();

        logger.info("Found customer, updating fields");

        existingCustomer.setFirstName(updatedCustomer.getFirstName());
        existingCustomer.setMiddleName(updatedCustomer.getMiddleName());
        existingCustomer.setLastName(updatedCustomer.getLastName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());

        return repository.save(existingCustomer);
    }

    public void deleteCustomer(UUID id) {
        logger.debug("Deleting customer with ID: {}", id);
        repository.deleteById(id);
    }
}
