package org.coffeeshop.users.repositories;
import java.util.Optional;
import org.coffeeshop.users.models.Staff;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StaffRepository extends JpaRepository<Staff, Long> {
    boolean existsById(@NotNull Long id);

    
    boolean existsByUsername(@NotNull String username);

    
    @Query("SELECT s FROM Staff s WHERE s.username = :username")
    Optional<Staff> findByUsername(@NotNull String username);

    
}
