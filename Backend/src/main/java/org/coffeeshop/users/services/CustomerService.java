package org.coffeeshop.users.services;

import jakarta.persistence.EntityNotFoundException;
import org.coffeeshop.Exceptions.UserExceptions.CustomerServiceException;
import org.coffeeshop.users.dtos.CustomerDto;
import org.coffeeshop.users.models.Customer;
import org.coffeeshop.users.repositories.CustomerRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


/**
 * this Service implementation is responsible for managing customer users in the coffee shop
 * and for implementing business logic related to customer operations.
 * @author Umunna David
 * @version 1.0
 * @since 2026-04-12
 *
 * */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Create a new customer (sync, like StaffService. Create).
     */
    public CustomerDto createCustomer(CustomerDto dto) {
        try {
            Customer newCustomer = Objects.requireNonNull(fromDtoCreate(dto), "Customer cannot be null");
            Customer saved = customerRepository.save(newCustomer);
            return toDto(saved);
        } catch (DataAccessException e) {
            throw new CustomerServiceException("Could not create customer", e);
        }
    }


    /**
     * Find all customers (sync, like StaffService. FindAll).
     * @return an unmodifiable list of all customers as DTOs
     */

    public List<CustomerDto> findAllCustomers() {
        List<Customer> customers = customerRepository.findAll(); 
        List<Customer> unmodifiableCustomers=Collections.unmodifiableList(customers);
        return unmodifiableCustomers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * creates a new cutomer asynchronously, similar to the create method but wrapped in a CompletableFuture.
     *  This allows the controller to handle the request without blocking the thread while waiting
     *  for the database operation to complete. The method also includes error handling to throw a CustomerServiceException 
     * if there is an issue during the creation process.
     * 
     * @return a CompletableFuture containing the created CustomerDto
     * @throws CustomerServiceException if there is an error during customer creation
     */
    @Async
    public CompletableFuture<CustomerDto> createCustomerAsync(CustomerDto dto) {
        try {
            Customer newCustomer = Objects.requireNonNull(fromDtoCreate(dto), "Customer cannot be null");
            Customer saved = customerRepository.save(newCustomer);
            return CompletableFuture.completedFuture(toDto(saved));
        } catch (DataAccessException e) {
            throw new CustomerServiceException("Could not create customer", e);
        }
    }

    /**
     * Find customer by id asynchronously.
     * @param id the ID of the customer to find
     * @return a CompletableFuture containing the found CustomerDto
     * @throws CustomerServiceException if there is an error during the find operation
     */
    @Async
    public CompletableFuture<CustomerDto> findCustomerById(Long id) {
        try {
            Objects.requireNonNull(id, "Customer ID cannot be null");
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));
            return CompletableFuture.completedFuture(toDto(customer));
        } catch (DataAccessException e) {
            throw new CustomerServiceException("Could not find customer with id " + id, e);
        }
    }

    /**
    * Update an existing customer asynchronously.
    * @param id the ID of the customer to update
    * @param dto the CustomerDto containing updated information
    * @return a CompletableFuture containing the updated CustomerDto
    * @throws CustomerServiceException if there is an error during the update operation
    */
    @Async
    public CompletableFuture<CustomerDto> updateCustomer(Long id, CustomerDto dto) {
        try {
            Objects.requireNonNull(id, "Customer ID cannot be null");
            Customer existing = customerRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));

                Customer updated = new Customer(
                    existing.getId(),
                    dto.customerFirstName(),
                    dto.customerLastName(),
                    dto.customerPhoneNumber()
                );

            Customer saved = customerRepository.save(updated);
            return CompletableFuture.completedFuture(toDto(saved));
        } catch (DataAccessException e) {
            throw new CustomerServiceException("Could not update customer with id " + id, e);
        }
    }
    /**
    * Delete a customer by ID asynchronously.
    * @param id the ID of the customer to delete
    * @return a CompletableFuture containing a success message upon deletion
    * @throws CustomerServiceException if there is an error during the delete operation
    */
    @Async
    public CompletableFuture<String> deleteCustomer(Long id) {
        try {
            Objects.requireNonNull(id, "Customer ID cannot be null");
            customerRepository.deleteById(id);
            customerRepository.flush();
            Map<String, String> resultMessage = new HashMap<>();
            resultMessage.put("message", "Customer Deleted");
            return CompletableFuture.completedFuture(resultMessage.get("message"));
        } catch (DataAccessException e) {
            throw new CustomerServiceException("Error deleting customer with id " + id, e);
        }
    }

    /**
     * Convert a Customer entity to a CustomerDto.
     * @param customer the Customer entity to convert
     * @return the corresponding CustomerDto
     */
    private CustomerDto toDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getCustomerFirstName(),
                customer.getCustomerLastName(),
                customer.getCustomerPhoneNumber()
        );
    }

    /**
     * Convert a CustomerDto to a Customer entity for creation.
     * @param customerDto the CustomerDto to convert
     * @return the corresponding Customer entity
     */
    private Customer fromDtoCreate(CustomerDto customerDto) {
        return new Customer(
                customerDto.customerFirstName(),
                customerDto.customerLastName(),
                customerDto.customerPhoneNumber()
        );
    }
}