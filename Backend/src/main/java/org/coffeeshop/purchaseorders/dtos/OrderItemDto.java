package org.coffeeshop.purchaseorders.dtos;

public record OrderItemDto(
        Long purchaseOrderId,
        Long menuItemTypeId,
        int quantity,
        double unitPrice,
        double lineTotal) {}
