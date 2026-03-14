package org.coffeeshop.purchaseorders.services;

import org.coffeeshop.purchaseorders.dtos.OrderItemDto;
import org.coffeeshop.purchaseorders.models.MenuItemType;
import org.coffeeshop.purchaseorders.models.OrderItem;
import org.coffeeshop.purchaseorders.models.OrderItemKey;
import org.coffeeshop.purchaseorders.models.PurchaseOrder;
import org.coffeeshop.purchaseorders.repositories.OrderItemRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service 
public class OrderItemService {
    private final OrderItemRepository repository;

    public OrderItemService(OrderItemRepository repository) {
        this.repository = repository;
    }

    public OrderItemDto getById(Long purchaseOrderId, Long menuItemId) {
    OrderItemKey key = new OrderItemKey(purchaseOrderId, menuItemId);
    OrderItem entity = repository.findById(key).orElseThrow(
        () -> new EntityNotFoundException(
            "OrderItem not found with purchaseOrderId: " + purchaseOrderId + 
            ", menuItemId: " + menuItemId));
    return toDto(entity);
}

    private OrderItemDto toDto(OrderItem entity) {
        PurchaseOrder po = entity.getPurchaseOrder();
        MenuItemType mu = entity.getMenuItemType();

        return new OrderItemDto(
            po != null ? po.getPurchaseOrderId() : 0L,
            mu != null ? mu.getMenuItemTypeId() : 0L,
            entity.getQuantity(),
            entity.getUnitPrice(),
            entity.getLineTotal()
        );
    }
}