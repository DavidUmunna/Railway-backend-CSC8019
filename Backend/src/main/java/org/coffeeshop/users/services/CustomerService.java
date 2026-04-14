package org.coffeeshop.users.services;

import jakarta.persistence.EntityNotFoundException;
import org.coffeeshop.Exceptions.UserExceptions.CustomerServiceException;
import org.coffeeshop.users.dtos.CustomerDto;
import org.coffeeshop.users.models.Customer;
import org.coffeeshop.users.repositories.CustomerRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
            Objects.requireNonNull(dto, "CustomerDto must not be null");
            Customer newCustomer = fromDtoCreate(dto);
            Customer saved = customerRepository.save(newCustomer);
            return toDto(saved);
        } catch (DataAccessException e) {
            throw new CustomerServiceException("Could not create customer", e);
        }
    }

    public List<CustomerDto> findAllCustomers() {
        List<Customer> customers = customerRepository.findAll(); // List<Customer> if JpaRepository
        return customers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Optionally, keep an async variant if you really need it.
     */
    @Async
    public CompletableFuture<CustomerDto> createCustomerAsync(CustomerDto dto) {
        try {
            Objects.requireNonNull(dto, "CustomerDto must not be null");
            Customer newCustomer = fromDtoCreate(dto);
            Customer saved = customerRepository.save(newCustomer);
            return CompletableFuture.completedFuture(toDto(saved));
        } catch (DataAccessException e) {
            throw new CustomerServiceException("Could not create customer", e);
        }
    }

    /**
     * Find customer by id asynchronously.
     */
    @Async
    public CompletableFuture<CustomerDto> findCustomerById(Long id) {
        try {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));
            return CompletableFuture.completedFuture(toDto(customer));
        } catch (DataAccessException e) {
            throw new CustomerServiceException("Could not find customer with id " + id, e);
        }
    }

    @Async
    public CompletableFuture<CustomerDto> updateCustomer(Long id, CustomerDto dto) {
        try {
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

    @Async
    public CompletableFuture<String> deleteCustomer(Long id) {
        try {
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
     * Mapping helpers, similar to StaffService.
     */

    private CustomerDto toDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getCustomerFirstName(),
                customer.getCustomerLastName(),
                customer.getCustomerPhoneNumber()
        );
    }

    // For create if you add an update path later, you can mirror Staff.fromDto
    private Customer fromDtoCreate(CustomerDto customerDto) {
        return new Customer(
                customerDto.customerFirstName(),
                customerDto.customerLastName(),
                customerDto.customerPhoneNumber()
        );
    }
}