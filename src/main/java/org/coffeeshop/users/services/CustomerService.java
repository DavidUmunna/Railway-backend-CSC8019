
package org.coffeeshop.users.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.coffeeshop.exceptions.userexceptions.CustomerServiceException;
import org.coffeeshop.users.dtos.CreateCustomerDto;
import org.coffeeshop.users.dtos.CustomerDto;
import org.coffeeshop.users.dtos.UpdateCustomerDto;
import org.coffeeshop.users.models.Customer;
import org.coffeeshop.users.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Umunna David
 * @version 1.0
 * @since 2026-04-12
 * @modifiedby Kulagina Tatiana
 * @since 2026-04-27
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
      * Create a new customer from the provided DTO.
      * @param dto the customer data transfer object containing the information needed to create a new customer
      * @return the created customer as a DTO
      * @throws CustomerServiceException if the customer could not be created
      */
    public CustomerDto createCustomer(CreateCustomerDto dto) {
        try {

            Customer newCustomer = fromDtoCreate(dto);
            if(newCustomer==null) {
                throw new IllegalArgumentException("Customer data is invalid");
            }
            Customer saved = customerRepository.save(newCustomer);
            return toDto(saved);
        } catch (DataAccessException e) {
            throw new CustomerServiceException("Could not create customer", e);
        }
    }

    public List<CustomerDto> findAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Find customer by id.
     */
    public CustomerDto findCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));
        return toDto(customer);
    }

    public CustomerDto updateCustomer(Long id, UpdateCustomerDto dto) {
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
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer entity = 
                customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));
        
        if (entity == null) {
            throw new EntityNotFoundException("Customer not found: " + id);
        }
        customerRepository.delete(entity);
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

    private Customer fromDtoCreate(CreateCustomerDto dto) {
        return new Customer(
                dto.customerFirstName(),
                dto.customerLastName(),
                dto.customerPhoneNumber()
        );
    }
}