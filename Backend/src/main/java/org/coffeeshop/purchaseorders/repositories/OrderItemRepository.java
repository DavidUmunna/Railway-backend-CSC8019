package org.coffeeshop.purchaseorders.repositories;

import org.coffeeshop.purchaseorders.models.OrderItem;
import org.coffeeshop.purchaseorders.models.OrderItemKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemKey> {}
