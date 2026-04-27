package org.coffeeshop.purchaseorders.services;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.coffeeshop.purchaseorders.dtos.MenuItemDto;
import org.coffeeshop.purchaseorders.dtos.MenuItemTypeDto;
import org.coffeeshop.purchaseorders.models.MenuItem;
import org.coffeeshop.purchaseorders.models.MenuItemType;
import org.coffeeshop.purchaseorders.repositories.MenuItemRepository;
import org.springframework.stereotype.Service;

/**
 * Service layer for menu item operations.
 * Retrieves menu items with their associated types (sizes and prices) nested.
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-18
 */
@Service
public class MenuItemService {
    private final MenuItemRepository repository;

    /**
     * Constructs the service with the given menu item repository.
     *
     * @param repository the menu item repository
     */
    public MenuItemService(MenuItemRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves a menu item by its ID, including nested size and price variants.
     *
     * @param id the menu item ID
     * @return the menu item as a DTO
     * @throws jakarta.persistence.EntityNotFoundException if no menu item exists with the given ID
     */
    public MenuItemDto getById(Long id) {
        MenuItem entity =
                repository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "MenuItem not found with id: " + id));
        return toDto(entity);
    }

    /**
     * Retrieves all menu items with their nested size and price variants.
     *
     * @return a list of all menu items as DTOs
     */
    public List<MenuItemDto> findAllMenuItems() {
        List<MenuItem> menuItems = repository.findAll();
        return menuItems.stream().map(this::toDto).collect(Collectors.toList());
    }

    // TBC if MenuItem can be added by staff. if so add createMenuItem method and deleteMenuItem

    /**
     * Converts a MenuItem entity to a MenuItemDto, including nested type variants.
     *
     * @param entity the menu item entity
     * @return the corresponding DTO
     */
    private MenuItemDto toDto(MenuItem entity) {
        List<MenuItemTypeDto> types =
                entity.getMenuItems().stream().map(this::toTypeDto).collect(Collectors.toList());

        return new MenuItemDto(
                entity.getMenuItemId(),
                entity.getName(),
                entity.getDescription(),
                entity.isAvailable(),
                types);
    }

    /**
     * Converts a MenuItemType entity to a MenuItemTypeDto.
     *
     * @param entity the menu item type entity
     * @return the corresponding DTO
     */
    private MenuItemTypeDto toTypeDto(MenuItemType entity) {
        MenuItem menuItem = entity.getMenuItem();
        return new MenuItemTypeDto(
                entity.getMenuItemTypeId(),
                menuItem != null ? menuItem.getMenuItemId() : 0L,
                entity.getSize(),
                entity.getPrice(),
                entity.isAvailable());
    }
}
