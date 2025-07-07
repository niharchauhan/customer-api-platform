package com.example.customer_api_service;

import com.example.customer_api_service.exception.DuplicateEmailException;
import com.example.customer_api_service.model.Customer;
import com.example.customer_api_service.repository.CustomerRepository;
import com.example.customer_api_service.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerApiServiceUnitTests {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(UUID.randomUUID());
        testCustomer.setFirstName("Alice");
        testCustomer.setMiddleName("B");
        testCustomer.setLastName("Smith");
        testCustomer.setEmail("alice@example.com");
        testCustomer.setPhoneNumber("+1-111-111-1111");
    }

    @Test
    void createCustomerSaveAndReturnCustomer() {
        when(customerRepository.findByEmail(testCustomer.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.save(testCustomer)).thenReturn(testCustomer);

        Customer created = customerService.createCustomer(testCustomer);

        assertEquals("Alice", created.getFirstName());
        verify(customerRepository, times(1)).save(testCustomer);
    }

    @Test
    void createCustomerThrowIfEmailExists() {
        when(customerRepository.findByEmail(testCustomer.getEmail())).thenReturn(Optional.of(testCustomer));

        assertThrows(DuplicateEmailException.class, () -> customerService.createCustomer(testCustomer));
    }

    @Test
    void getCustomerByIdReturnCustomer() {
        UUID id = testCustomer.getId();
        when(customerRepository.findById(id)).thenReturn(Optional.of(testCustomer));

        Optional<Customer> found = customerService.getCustomerById(id);

        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getFirstName());
    }

    @Test
    void updateCustomerFields() {
        UUID id = testCustomer.getId();
        Customer updated = new Customer();
        updated.setFirstName("Updated");
        updated.setMiddleName("M");
        updated.setLastName("User");
        updated.setEmail("updated@example.com");
        updated.setPhoneNumber("999-999-9999");

        when(customerRepository.findById(id)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(ArgumentMatchers.any(Customer.class))).thenReturn(updated);

        Customer result = customerService.updateCustomer(id, updated);

        assertEquals("Updated", result.getFirstName());
        assertEquals("updated@example.com", result.getEmail());
        verify(customerRepository).save(testCustomer);
    }

    @Test
    void deleteCustomerAndCallRepository() {
        UUID id = testCustomer.getId();
        doNothing().when(customerRepository).deleteById(id);

        customerService.deleteCustomer(id);

        verify(customerRepository, times(1)).deleteById(id);
    }
}
