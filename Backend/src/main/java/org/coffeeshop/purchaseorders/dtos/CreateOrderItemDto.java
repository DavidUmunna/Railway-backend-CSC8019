package org.coffeeshop.purchaseorders.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderItemDto(@NotNull Long menuItemTypeId, @Positive int quantity) {}
