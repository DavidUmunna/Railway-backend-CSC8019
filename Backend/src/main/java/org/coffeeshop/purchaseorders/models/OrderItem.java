package org.coffeeshop.purchaseorders.models;

import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @EmbeddedId private OrderItemKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("purchaseOrderId")
    @JoinColumn(name = "purchase_order_id", insertable = false, updatable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("menuItemTypeId")
    @JoinColumn(name = "menu_item_type_id", insertable = false, updatable = false)
    private MenuItemType menuItemType;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name = "line_total")
    private double lineTotal;

    protected OrderItem() {}

    public OrderItem(
            PurchaseOrder purchaseOrder,
            MenuItemType menuItemType,
            int quantity,
            double unitPrice,
            double lineTotal) {
        this.purchaseOrder = purchaseOrder;
        this.menuItemType = menuItemType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    public OrderItem(MenuItemType menuItemType, int quantity, double unitPrice, double lineTotal) {
        this(null, menuItemType, quantity, unitPrice, lineTotal);
    }

    public OrderItemKey getId() {
        return id;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public MenuItemType getMenuItemType() {
        return menuItemType;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getLineTotal() {
        return lineTotal;
    }
}
