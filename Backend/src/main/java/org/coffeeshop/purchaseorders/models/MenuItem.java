package org.coffeeshop.purchaseorders.models;

import java.util.List;

import jakarta.persistence.*;

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

    public MenuItem() {}

    public void setItemName(String name) {
        this.name = name;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    public Long getMenuItemId() {
        return menuItemId;
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
