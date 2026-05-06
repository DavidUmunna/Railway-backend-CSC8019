package org.coffeeshop.users.repositories;

import java.util.Optional;

import org.coffeeshop.users.models.Staff;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Staff entities in the database.
 * It extends JpaRepository to provide CRUD operations and includes methods to check for the existence of a staff by username.
 * 
 * @author Umunna David
 * @version 1.0
 * @since 2026-04-12
 * @modifiedby Kulagina Tatiana
 * @since 2026-04-12
 */
public interface StaffRepository extends JpaRepository<Staff, Long> {
    boolean existsByUsername(@NotNull String username);
    Optional<Staff> findByUsername(@NotNull String username);

    boolean existsByStaffId(@NotNull Long staffId);
}
