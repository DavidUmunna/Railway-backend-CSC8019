package org.coffeeshop.users.repositories;

import org.coffeeshop.users.models.Staff;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/****
 * Repository interface for managing Staff entities in the database.
 * It extends JpaRepository to provide CRUD operations and includes methods to check for the existence of a staff member by ID and username.
 * 
 * @author Umunna David
 * @version 1.0
 * @since 2026-04-12
 */
public interface StaffRepository extends JpaRepository<Staff, Long> {
    boolean existsById(@NotNull Long id);

    boolean existsByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    Staff findByUsername(String username);
}
