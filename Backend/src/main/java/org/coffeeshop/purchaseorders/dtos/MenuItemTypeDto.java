package org.coffeeshop.purchaseorders.dtos;

import org.coffeeshop.purchaseorders.models.MenuItemSize;

public record MenuItemTypeDto(
        Long menuItemTypeId,
        Long menuItemId,
        MenuItemSize size,
        double price,
        boolean isAvailable) {}
