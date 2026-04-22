package org.coffeeshop.usertests.customertests;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Objects;



/**
 * Test class for CustomerController.
 * It uses Spring Boot's testing support to perform integration tests on the CustomerController endpoints.
 * The tests cover creating, retrieving, and listing customers.
 * all tests are transactional and will not persist in the database/store
 * @author Umunna David
 * @version 1.0
 * @since 2026-04-12
 */
@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;
    /*@MockBean
    JwtService jwtService;
    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;*/
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    /**
     * this test asserts if  if a particular customer is created,
     * it makes use of mockMvc to perform a post request
     * which is expetcted to be created and expected to have all the customers data 
     * saved in the right field  
     * @throws Exception 
     */
    @Test
    void createCustomer_returnsCreatedCustomer() throws Exception {
         CustomerDto request = new CustomerDto(null, "Jane", "Grande", "07123456789");

        mockMvc.perform(post("/api/v1/customers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").isNumber())
                .andExpect(jsonPath("$.customerFirstName").value("Jane"))
                .andExpect(jsonPath("$.customerLastName").value("Grande"))
                .andExpect(jsonPath("$.customerPhoneNumber").value("07123456789"));

        assertTrue(customerRepository.findAll().stream()
                .anyMatch(customer -> "07123456789".equals(customer.getCustomerPhoneNumber())));
    }

    @Test
    void getAllCustomers_returnsList() throws Exception {
        customerRepository.save(new Customer("Jane", "Grande", "07123456789"));
        customerRepository.save(new Customer("John", "Doe", "07000000000"));
        customerRepository.flush();
        entityManager.clear();

        mockMvc.perform(get("/api/v1/customers/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].customerPhoneNumber", containsInAnyOrder("07123456789", "07000000000")));
    }

    @Test
    void getCustomerById_returnsCustomer() throws Exception {
        Customer savedCustomer = customerRepository.save(
                new Customer("Jane", "Grande", "07123456789")
        );
        customerRepository.flush();
        entityManager.clear();

        mockMvc.perform(get("/api/v1/customers/{id}", savedCustomer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(savedCustomer.getId()))
                .andExpect(jsonPath("$.customerFirstName").value("Jane"))
                .andExpect(jsonPath("$.customerPhoneNumber").value("07123456789"));
    }

    /**
     * This test asserts if a particular customer is updated successfully
     * it makes use of mockMvc to perform a put request to update the customer details
     * the request is expected to be successful and the response should contain the updated customer details
     * @throws Exception
     */
    @Test
    void updateCustomer_returnsUpdatedCustomer() throws Exception {
        Customer savedCustomer = customerRepository.save(
                new Customer("Jane", "Grande", "07123456789")
        );
        customerRepository.flush();
        entityManager.clear();

        CustomerDto updatedCustomer = new CustomerDto(
                savedCustomer.getId(),
                "John",
                "Doe",
                "07011112222"
        );

        String json = objectMapper.writeValueAsString(updatedCustomer);


        mockMvc.perform(put("/api/v1/customers/{id}", savedCustomer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(savedCustomer.getId()))
                .andExpect(jsonPath("$.customerFirstName").value("John"))
                .andExpect(jsonPath("$.customerLastName").value("Doe"))
                .andExpect(jsonPath("$.customerPhoneNumber").value("07011112222"));
        Long customerId = savedCustomer.getId();
        Objects.requireNonNull(customerId, "Saved customer ID should not be null");
        assertTrue(customerRepository.findById(customerId)
                .map(customer -> "07011112222".equals(customer.getCustomerPhoneNumber()))
                .orElse(false));
    }

        /**
         * This test asserts if a particular customer is deleted successfully
         * it makes use of mockMvc to perform a delete request to delete the customer
         * the request is expected to be successful and the response should contain a message confirming the deletion
         * after the deletion, the test also checks that the customer no longer exists in the repository
         * @throws Exception
         */
    @Test
    void deleteCustomer_returnsStringMessage() throws Exception {
        Customer savedCustomer = customerRepository.save(
                new Customer("Jane", "Grande", "07123456789")
        );
        customerRepository.flush();
        entityManager.clear();

        mockMvc.perform(delete("/api/v1/customers/{id}", savedCustomer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Customer Deleted Successfully"));

        assertFalse(customerRepository.existsById(savedCustomer.getId()));
    }
}