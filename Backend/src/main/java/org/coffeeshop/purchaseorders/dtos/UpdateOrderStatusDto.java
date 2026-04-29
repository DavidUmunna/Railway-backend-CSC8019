package org.coffeeshop.purchaseorders.dtos;

import jakarta.validation.constraints.NotNull;
import org.coffeeshop.purchaseorders.models.OrderStatus;

/**
 * Request DTO for updating the status of an existing purchase order.
 *
 * @param orderStatus the new status to set (must not be null)
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-18
 */
public record UpdateOrderStatusDto(@NotNull OrderStatus orderStatus) {}
