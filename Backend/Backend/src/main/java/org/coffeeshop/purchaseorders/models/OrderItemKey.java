package org.coffeeshop.purchaseorders.models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key for {@link OrderItem}, consisting of
 * a purchase order ID and a menu item type ID.
 * Implements {@code Serializable} as required by JPA for composite keys.
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-18
 */
@Embeddable
public class OrderItemKey implements Serializable {
    /** The ID of the parent purchase order. */
    private Long purchaseOrderId;

    /** The ID of the associated menu item type. */
    private Long menuItemTypeId;

    /** No-arg constructor required by JPA. */
    protected OrderItemKey() {}

    /**
     * Constructs a composite key from the given IDs.
     *
     * @param purchaseOrderId the purchase order ID
     * @param menuItemTypeId  the menu item type ID
     */
    public OrderItemKey(Long purchaseOrderId, Long menuItemTypeId) {
        this.purchaseOrderId = purchaseOrderId;
        this.menuItemTypeId = menuItemTypeId;
    }

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public Long getMenuItemTypeId() {
        return menuItemTypeId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OrderItemKey)) return false;

        OrderItemKey other = (OrderItemKey) obj;

        return Objects.equals(other.purchaseOrderId, purchaseOrderId)
                && Objects.equals(other.menuItemTypeId, menuItemTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseOrderId, menuItemTypeId);
    }
}
