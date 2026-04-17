package org.coffeeshop.purchaseorders.dtos;

import jakarta.validation.constraints.NotNull;
import org.coffeeshop.purchaseorders.models.OrderStatus;

public record UpdateOrderStatusDto(@NotNull OrderStatus orderStatus) {}
