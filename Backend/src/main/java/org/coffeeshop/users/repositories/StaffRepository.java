package org.coffeeshop.users.repositories;
import java.util.function.BooleanSupplier;

import org.coffeeshop.users.models.Staff;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StaffRepository extends JpaRepository<Staff, Long> {
    boolean existsById(@NotNull Long id);

    BooleanSupplier existsByUsername(String string);
}
