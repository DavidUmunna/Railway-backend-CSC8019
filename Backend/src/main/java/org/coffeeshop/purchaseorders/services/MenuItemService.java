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

@Service
public class MenuItemService {
    private final MenuItemRepository repository;

    public MenuItemService(MenuItemRepository repository) {
        this.repository = repository;
    }

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

    public List<MenuItemDto> findAllMenuItems() {
        List<MenuItem> menuItems = repository.findAll();
        return menuItems.stream().map(this::toDto).collect(Collectors.toList());
    }

    // TBC if MenuItem can be added by staff. if so add createMenuItem method and deleteMenuItem

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
