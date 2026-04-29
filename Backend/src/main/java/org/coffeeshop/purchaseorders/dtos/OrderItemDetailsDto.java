package org.coffeeshop.purchaseorders.dtos;

/**
 * DTO for detailed order item data, including menu item info.
 * used for response payload when fetching data to populate order cards in the frontend.
 * @author Umunna David
 * @version 1.0
 * @since 24/04/2026
 */
public record OrderItemDetailsDto(
    Long purchaseOrderId,
    Long menuItemTypeId,
    String itemName,
    String itemDescription,
    String size,
    double unitPrice,
    int quantity,
    double lineTotal
) {}
