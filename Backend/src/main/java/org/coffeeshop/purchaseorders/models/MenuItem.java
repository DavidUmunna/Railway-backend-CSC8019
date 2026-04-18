package org.coffeeshop.purchaseorders.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "menu_item")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_item_id")
    private Long menuItemId;

    @OneToMany(mappedBy = "menuItem")
    private List<MenuItemType> menuItems;

    @Column(name = "item_name")
    private String name;

    @Column(name = "item_description")
    private String description;

    @Column(name = "is_available")
    private boolean isAvailable;

    protected MenuItem() {}

    public MenuItem(
            Long menuItemId,
            String name,
            String description,
            boolean isAvailable) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.description = description;
        this.isAvailable = isAvailable;
    }

    public MenuItem(String name, String description, boolean isAvailable) {
        this(null, name, description, isAvailable);
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public List<MenuItemType> getMenuItems() {
        return menuItems;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
