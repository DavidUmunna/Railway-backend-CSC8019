package org.coffeeshop.purchaseorders.models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderItemKey implements Serializable {
    private Long purchaseOrderId;
    private Long menuItemTypeId;

    protected OrderItemKey() {}

    public OrderItemKey(Long purchaseOrderId, Long menuItemTypeId) {
        this.purchaseOrderId = purchaseOrderId;
        this.menuItemTypeId = menuItemTypeId;
    }

    // not sure if these setters are needed or removed
    // TODO write tests and decide
    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public void setMenuItemTypeId(Long menuItemTypeId) {
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
