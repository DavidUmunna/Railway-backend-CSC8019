package org.coffeeshop.purchaseorders.repositories;

import org.coffeeshop.purchaseorders.models.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {}