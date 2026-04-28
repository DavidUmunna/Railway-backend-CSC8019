package org.coffeeshop.purchaseorders.dtos;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating a new menu item.
 * Contains the item details and a nested list of size and price variants.
 *
 * @param name         the display name of the menu item (required)
 * @param description  a description of the menu item
 * @param isAvailable  whether the menu item is currently available for ordering
 * @param menuItemTypes the list of size and price variants for this menu item (must not be empty)
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-23
 */
public record CreateMenuItemDto(
    @NotNull String name,
    String description,
    boolean isAvailable,
    @NotEmpty List<@Valid CreateMenuItemTypeDto> menuItemTypes) {}