package org.coffeeshop.purchaseorders.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Request DTO for a single order item within a purchase order creation request.
 * References a menu item type and specifies the quantity.
 *
 * @param menuItemTypeId the ID of the menu item type being ordered (must not be null)
 * @param quantity       the number of units ordered (must be positive)
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-18
 */
public record CreateOrderItemDto(@NotNull Long menuItemTypeId, @Positive int quantity) {}
