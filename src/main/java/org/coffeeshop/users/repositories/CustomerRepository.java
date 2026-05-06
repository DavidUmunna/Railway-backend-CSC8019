package org.coffeeshop.users.repositories;

import java.util.List;

import org.coffeeshop.users.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Customer entities in the database.
 * 
 * @author Umunna David
 * @version 1.0
 * @since 2026-04-12
 * @modifiedby Kulagina Tatiana
 * @since 2026-04-12
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsById(Long id);


    /**
     * Finds a customer by their phone number.
     *
     * @param customerPhoneNumber the phone number of the customer
     * @return the customer entity matching the given phone number
     */
    List<Customer> findByCustomerPhoneNumber(String customerPhoneNumber);

    /**
     * Checks if a customer exists with the given phone number.
     *
     * @param customerPhoneNumber the phone number to check for existence
     * @return true if a customer with the given phone number exists, false otherwise
     */
    boolean existsByCustomerPhoneNumber(String customerPhoneNumber);

}
