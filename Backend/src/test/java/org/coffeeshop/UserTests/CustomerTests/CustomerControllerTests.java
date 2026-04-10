package org.coffeeshop.UserTests.CustomerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.coffeeshop.users.dtos.CustomerDto;
import org.coffeeshop.users.models.Customer;
import org.coffeeshop.users.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void createCustomer_returnsCreatedCustomer() throws Exception {
                CustomerDto request = new CustomerDto(null, "Jane", "Grande", "07123456789");

        mockMvc.perform(post("/api/v1/customers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customer_id").isNumber())
                .andExpect(jsonPath("$.customer_firstname").value("Jane"))
                .andExpect(jsonPath("$.customer_lastname").value("Grande"))
                .andExpect(jsonPath("$.customer_phone_number").value("07123456789"));

        assertTrue(customerRepository.findAll().stream()
                .anyMatch(customer -> "07123456789".equals(customer.getCustomerPhoneNumber())));
    }

    @Test
    void getAllCustomers_returnsList() throws Exception {
                customerRepository.save(new Customer("Jane", "Grande", "07123456789"));
                customerRepository.save(new Customer("John", "Doe", "07000000000"));

        mockMvc.perform(get("/api/v1/customers/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].customer_phone_number", containsInAnyOrder("07123456789", "07000000000")));
    }

    @Test
    void getCustomerById_returnsCustomer() throws Exception {
        Customer savedCustomer = customerRepository.save(
                new Customer("Jane", "Grande", "07123456789")
        );

        MvcResult results = mockMvc.perform(get("/api/v1/customers/{id}", savedCustomer.getId()))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(results))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer_id").value(savedCustomer.getId()))
                .andExpect(jsonPath("$.customer_firstname").value("Jane"))
                .andExpect(jsonPath("$.customer_phone_number").value("07123456789"));
    }

    @Test
    void updateCustomer_returnsUpdatedCustomer() throws Exception {
        Customer savedCustomer = customerRepository.save(
                new Customer("Jane", "Grande", "07123456789")
        );

        CustomerDto updatedCustomer = new CustomerDto(
                savedCustomer.getId(),
                "John",
                "Doe",
                "07011112222"
        );

        String json = objectMapper.writeValueAsString(updatedCustomer);
        MvcResult results = mockMvc.perform(put("/api/v1/customers/{id}", savedCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(results))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer_id").value(savedCustomer.getId()))
                .andExpect(jsonPath("$.customer_firstname").value("John"))
                .andExpect(jsonPath("$.customer_lastname").value("Doe"))
                .andExpect(jsonPath("$.customer_phone_number").value("07011112222"));

        assertTrue(customerRepository.findById(savedCustomer.getId())
                .map(customer -> "07011112222".equals(customer.getCustomerPhoneNumber()))
                .orElse(false));
    }

    @Test
    void deleteCustomer_returnsStringMessage() throws Exception {
        Customer savedCustomer = customerRepository.save(
                new Customer("Jane", "Grande", "07123456789")
        );

        MvcResult result = mockMvc.perform(delete("/api/v1/customers/{id}", savedCustomer.getId()))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Customer Deleted"));

        assertFalse(customerRepository.existsById(savedCustomer.getId()));
    }
}