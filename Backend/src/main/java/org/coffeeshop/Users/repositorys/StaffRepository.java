package org.coffeeshop.Users.repositorys;

import org.coffeeshop.Users.models.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    // "create staff" → use repo.save(staff) in your StaffService

    // "get staff" by id → use repo.findById(id) (inherited from JpaRepository)

    // Custom "get staff" finders:
    Optional<Staff> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
