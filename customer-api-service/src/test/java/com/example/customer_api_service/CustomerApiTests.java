package com.example.customer_api_service;

import com.example.customer_api_service.model.Customer;
import com.example.customer_api_service.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository repository;

    private static final String BASE_URL = "/api/customers";

    private static UUID createdCustomerId;

    @Test
    @Order(1)
    public void testCreateCustomer() throws Exception {
        // Create a sample customer object
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setMiddleName("M");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhoneNumber("+1-234-567-8901");

        // Convert the object to JSON
        String json = objectMapper.writeValueAsString(customer);

        // Step 1: Prepare the request
        MockHttpServletRequestBuilder requestBuilder = post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // Step 2: Perform request and validate response
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andReturn();

        // Step 3: Extract response body
        String response = result.getResponse().getContentAsString();
        Customer created = objectMapper.readValue(response, Customer.class);

        // Step 4: Save the created ID for later use
        createdCustomerId = created.getId();
        assertTrue(createdCustomerId != null);
    }

    @Test
    @Order(2)
    public void testGetAllCustomers() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(3)
    public void testGetCustomerById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + createdCustomerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @Order(4)
    public void testUpdateCustomer() throws Exception {
        Customer updated = new Customer();
        updated.setFirstName("Johnny");
        updated.setMiddleName("X");
        updated.setLastName("Doe");
        updated.setEmail("john.doe@example.com");
        updated.setPhoneNumber("+1-000-111-2222");

        mockMvc.perform(put(BASE_URL + "/" + createdCustomerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Johnny"));
    }

    @Test
    @Order(5)
    public void testDuplicateEmailReturnsBadRequest() throws Exception {
        Customer duplicate = new Customer();
        duplicate.setFirstName("Jane");
        duplicate.setLastName("Smith");
        duplicate.setEmail("john.doe@example.com"); // duplicate
        duplicate.setPhoneNumber("+91-123-456-7890");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("already exists")));
    }

    @Test
    @Order(6)
    public void testGetCustomerByInvalidId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    public void testUpdateNonExistentCustomer() throws Exception {
        Customer dummy = new Customer();
        dummy.setFirstName("Ghost");
        dummy.setLastName("User");
        dummy.setEmail("ghost@example.com");
        dummy.setPhoneNumber("000");

        mockMvc.perform(put(BASE_URL + "/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummy)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Customer not found")));
    }

    @Test
    @Order(8)
    public void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + createdCustomerId))
                .andExpect(status().isNoContent());

        Optional<Customer> deleted = repository.findById(createdCustomerId);
        assertTrue(deleted.isEmpty());
    }

    @Test
    @Order(9)
    public void testDeleteNonExistentCustomer() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + UUID.randomUUID()))
                .andExpect(status().isNoContent()); // Spring Data deleteById is idempotent
    }
}
