package org.coffeeshop.purchaseorders.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import org.coffeeshop.purchaseorders.models.OrderStatus;

public record PurchaseOrderDto(
    Long orderId,
    Long customerId,
    Long stationId,
    LocalDate orderDate, 
    LocalTime pickupTime, 
    OrderStatus orderStatus, 
    double totalAmount 
) {}