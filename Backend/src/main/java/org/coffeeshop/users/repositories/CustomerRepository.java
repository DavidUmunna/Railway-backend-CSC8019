package org.coffeeshop.users.repositories;

import org.coffeeshop.users.models.Customer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsById(@NotNull Long id);
}
