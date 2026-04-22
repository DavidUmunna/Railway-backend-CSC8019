package org.coffeeshop.purchaseorders.repositories;

import org.coffeeshop.purchaseorders.models.MenuItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link org.coffeeshop.purchaseorders.models.MenuItemType} entities.
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-18
 */
@Repository
public interface MenuItemTypeRepository extends JpaRepository<MenuItemType, Long> {}
