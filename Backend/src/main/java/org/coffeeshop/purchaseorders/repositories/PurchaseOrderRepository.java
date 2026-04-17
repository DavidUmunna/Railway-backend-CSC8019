package org.coffeeshop.purchaseorders.repositories;

import org.coffeeshop.purchaseorders.models.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {}
