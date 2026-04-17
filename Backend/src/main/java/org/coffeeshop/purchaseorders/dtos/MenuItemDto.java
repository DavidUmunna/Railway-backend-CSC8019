package org.coffeeshop.purchaseorders.dtos;

import java.util.List;

public record MenuItemDto(
        Long id,
        String name,
        String description,
        boolean isAvailable,
        List<MenuItemTypeDto> types) {}
