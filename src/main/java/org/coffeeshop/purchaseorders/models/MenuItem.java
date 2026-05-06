package org.coffeeshop.purchaseorders.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity representing a menu item available for ordering.
 * Each menu item has a name, description, availability flag,
 * and a list of associated types (sizes and prices).
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-23
 */
@Entity
@Table(name = "menu_item")
public class MenuItem {

    /** Auto-generated primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_item_id")
    private Long menuItemId;

    /** The size and price variants available for this menu item. */
    @OneToMany(mappedBy = "menuItem", orphanRemoval = true)
    private List<MenuItemType> menuItems;

    /** The display name of the menu item. */
    @Column(name = "item_name")
    private String name;

    /** A description of the menu item. */
    @Column(name = "item_description")
    private String description;

    /** Whether this menu item is currently available for ordering. */
    @Column(name = "is_available")
    private boolean isAvailable;

    /** No-arg constructor required by JPA. */
    protected MenuItem() {
        menuItems = new ArrayList<>();
    }

    /**
     * Full constructor including the menu item ID.
     *
     * @param menuItemId  the primary key (null for new entities)
     * @param name        the display name
     * @param description the item description
     * @param isAvailable the availability flag
     */
    public MenuItem(
            Long menuItemId,
            String name,
            String description,
            boolean isAvailable) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.description = description;
        this.isAvailable = isAvailable;
        menuItems = new ArrayList<>();
    }

    /**
     * Constructor without ID for creating new entities.
     *
     * @param name        the display name
     * @param description the item description
     * @param isAvailable the availability flag
     */
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
