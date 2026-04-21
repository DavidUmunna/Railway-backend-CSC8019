package org.coffeeshop.purchaseorders.models;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_item_type")
public class MenuItemType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_item_type_id")
    private Long menuItemTypeId;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "size_name")
    private MenuItemSize size;

    @Column(name = "price")
    private double price;

    @Column(name = "is_available")
    private boolean isAvailable;

    protected MenuItemType() {}

    public MenuItemType(
            Long menuItemTypeId,
            MenuItem menuItem,
            MenuItemSize size,
            double price,
            boolean isAvailable) {
        this.menuItemTypeId = menuItemTypeId;
        this.menuItem = menuItem;
        this.size = size;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public MenuItemType(MenuItem menuItem, MenuItemSize size, double price, boolean isAvailable) {
        this(null, menuItem, size, price, isAvailable);
    }

    public Long getMenuItemTypeId() {
        return menuItemTypeId;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public MenuItemSize getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
