package org.coffeeshop.purchaseorders.models;

import jakarta.persistence.*;

/**
 * JPA entity representing a line item within a purchase order.
 * Uses a composite key ({@link OrderItemKey}) derived from the parent
 * purchase order and the associated menu item type via {@code @MapsId}.
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-18
 */
@Entity
@Table(name = "order_item")
public class OrderItem {

    /** Composite primary key derived from purchase order and menu item type IDs. */
    @EmbeddedId private OrderItemKey id;

    /** The parent purchase order this line item belongs to. */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("purchaseOrderId")
    @JoinColumn(name = "purchase_order_id", insertable = false, updatable = false)
    private PurchaseOrder purchaseOrder;

    /** The menu item type being ordered. */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("menuItemTypeId")
    @JoinColumn(name = "menu_item_type_id", insertable = false, updatable = false)
    private MenuItemType menuItemType;

    /** The quantity ordered. */
    @Column(name = "quantity")
    private int quantity;

    /** The unit price at the time of order. */
    @Column(name = "unit_price")
    private double unitPrice;

    /** The line total (unitPrice * quantity). */
    @Column(name = "line_total")
    private double lineTotal;

    /** No-arg constructor required by JPA. */
    protected OrderItem() {}

    /**
     * Full constructor with parent order reference, used when persisting after the order is saved.
     *
     * @param purchaseOrder the parent purchase order
     * @param menuItemType  the menu item type being ordered
     * @param quantity      the quantity ordered
     * @param unitPrice     the unit price at time of order
     * @param lineTotal     the line total (unitPrice * quantity)
     */
    public OrderItem(
            PurchaseOrder purchaseOrder,
            MenuItemType menuItemType,
            int quantity,
            double unitPrice,
            double lineTotal) {
        this.id = new OrderItemKey();
        this.purchaseOrder = purchaseOrder;
        this.menuItemType = menuItemType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    /**
     * Constructor without parent order reference, used during order creation
     * before the order has been persisted.
     *
     * @param menuItemType the menu item type being ordered
     * @param quantity     the quantity ordered
     * @param unitPrice    the unit price at time of order
     * @param lineTotal    the line total (unitPrice * quantity)
     */
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
