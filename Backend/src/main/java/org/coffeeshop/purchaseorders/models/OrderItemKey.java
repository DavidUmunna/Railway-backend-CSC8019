package org.coffeeshop.purchaseorders.models;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;

@Embeddable
public class OrderItemKey implements Serializable {
    private Long purchaseOrderId;
    private Long menuItemTypeId;

    public OrderItemKey() {}

    public OrderItemKey(Long purchaseOrderId, Long menuItemTypeId) {
        this.purchaseOrderId = purchaseOrderId;
        this.menuItemTypeId = menuItemTypeId;
    }

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
