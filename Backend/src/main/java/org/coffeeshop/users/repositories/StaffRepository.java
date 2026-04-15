package org.coffeeshop.users.repositories;

import java.util.Optional;

import org.coffeeshop.users.models.Staff;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StaffRepository extends JpaRepository<Staff, Long> {
    boolean existsById(@NotNull Long id);

    boolean existsByUsername(@NotNull String username);

    Optional<Staff> findByUsername(@NotNull String username);
}
