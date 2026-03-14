package org.coffeeshop.purchaseorders.dtos;

import org.coffeeshop.purchaseorders.models.MenuItemSize;

public record MenuItemTypeResponseDto(
    Long menuItemTypeId, 
    Long menuItemId,
    MenuItemSize size, 
    double price, 
    boolean isAvailable
) {}