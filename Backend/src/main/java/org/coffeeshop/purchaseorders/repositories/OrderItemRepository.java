package org.coffeeshop.purchaseorders.repositories;

import org.coffeeshop.purchaseorders.models.OrderItem;
import org.coffeeshop.purchaseorders.models.OrderItemKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link org.coffeeshop.purchaseorders.models.OrderItem} entities.
 * Uses composite key {@link org.coffeeshop.purchaseorders.models.OrderItemKey}.
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-18
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemKey> {}
