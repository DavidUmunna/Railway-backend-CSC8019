package org.coffeeshop.purchaseorders.repositories;

import org.coffeeshop.purchaseorders.models.MenuItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemTypeRepository extends JpaRepository<MenuItemType, Long> {}
