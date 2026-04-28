package org.coffeeshop.purchaseorders.dtos;

import org.coffeeshop.purchaseorders.models.MenuItemSize;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Request DTO for creating a new menu item type (size variant).
 * Represents a single size and price combination within a menu item.
 *
 * @param size  the size variant of the menu item type (required, must be a valid {@link MenuItemSize})
 * @param price the price for this size variant (must be positive)
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-23
 */
public record CreateMenuItemTypeDto(@NotNull MenuItemSize size, @Positive double price) {}