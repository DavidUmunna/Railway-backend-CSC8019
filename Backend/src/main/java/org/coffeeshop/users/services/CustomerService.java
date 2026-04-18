
package org.coffeeshop.users.services;

import jakarta.persistence.EntityNotFoundException;
import org.coffeeshop.exceptions.UserExceptions.CustomerServiceException;
import org.coffeeshop.users.dtos.CustomerDto;
import org.coffeeshop.users.models.Customer;
import org.coffeeshop.users.repositories.CustomerRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/****
 * Service for managing customer users in the coffee shop
 * also for implementing business logic
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
     * Create a new customer from the provided DTO.
      * @param dto the customer data transfer object containing the information needed to create a new customer
     */
    public CustomerDto createCustomer(CustomerDto dto) {
        try {
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
     * Find customer by id.
     */
    public CustomerDto findCustomerById(Long id) {
        try {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));
            return toDto(customer);
        } catch (DataAccessException e) {
            throw new CustomerServiceException("Could not find customer with id " + id, e);
        }
    }

    
    public CustomerDto updateCustomer(Long id, CustomerDto dto) {
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
            return toDto(saved);
        } catch (DataAccessException e) {
            throw new CustomerServiceException("Could not update customer with id " + id, e);
        }
    }

    
    public String deleteCustomer(Long id) {
        try {
            customerRepository.deleteById(id);
            customerRepository.flush();
          return "Customer Deleted Successfully";
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