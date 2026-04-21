package org.coffeeshop.purchaseorders.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

public record CreatePurchaseOrderDto(
        @NotNull Long customerId,
        @NotNull Long stationId,
        @NotNull LocalTime pickupTime,
        @NotEmpty List<@Valid CreateOrderItemDto> orderItems) {}
