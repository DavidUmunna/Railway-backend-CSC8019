package org.coffeeshop.purchaseorders.dtos;

import java.util.List;

/**
 * Response DTO for menu item data.
 * Contains the item details and a nested list of available types (sizes and prices).
 *
 * @param id          the unique menu item identifier
 * @param name        the display name of the menu item
 * @param description a description of the menu item
 * @param isAvailable whether the menu item is currently available
 * @param types       the list of size and price variants for this menu item
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-27
 */
public record MenuItemDto(
        Long menuItemId,
        String name,
        String description,
        boolean isAvailable,
        List<MenuItemTypeDto> menuItemTypes) {}
