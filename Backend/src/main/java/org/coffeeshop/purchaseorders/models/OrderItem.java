package org.coffeeshop.purchaseorders.models;

import jakarta.persistence.*;

@Entity
@Table(name="order_item")
public class OrderItem {

    @EmbeddedId
    private OrderItemKey id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("purchaseOrderId") 
    @JoinColumn(name = "purchase_order_id", insertable = false, updatable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("menuItemTypeId")
    @JoinColumn(name = "menu_item_type_id", insertable = false, updatable = false)
    private MenuItemType menuItemType;

    @Column (name = "quantity")
    private int quantity;
    
    @Column (name = "unit_price")
    private double unitPrice;

    @Column (name = "line_total")
    private double lineTotal;

    public OrderItem() {}

    public void setId(OrderItemKey id) {
        this.id = id;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public void setMenuItemType(MenuItemType menuItemType) {
        this.menuItemType = menuItemType;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
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
