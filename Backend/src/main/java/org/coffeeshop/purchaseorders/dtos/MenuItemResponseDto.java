package org.coffeeshop.purchaseorders.dtos;

// TBC if frontend might request this information. if no, delete

public record MenuItemResponseDto (
    Long id,
    String name,
    String description,
    boolean isAvailable
) {}
