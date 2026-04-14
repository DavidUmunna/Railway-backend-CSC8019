package org.coffeeshop.users.repositories;

import org.coffeeshop.users.models.Customer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Customer entities in the database.
 * It extends JpaRepository to provide CRUD operations and includes a method to check for the existence of a customer by ID.
 * 
 * @author Umunna David
 * @version 1.0
 * @since 2026-04-12
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsById(@NotNull Long id);
}
