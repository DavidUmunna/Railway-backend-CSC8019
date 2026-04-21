package org.coffeeshop.purchaseorders.services;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.coffeeshop.purchaseorders.dtos.MenuItemTypeDto;
import org.coffeeshop.purchaseorders.models.MenuItem;
import org.coffeeshop.purchaseorders.models.MenuItemType;
import org.coffeeshop.purchaseorders.repositories.MenuItemTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuItemTypeService {
    private final MenuItemTypeRepository repository;

    public MenuItemTypeService(MenuItemTypeRepository repository) {
        this.repository = repository;
    }

    public MenuItemTypeDto getById(Long id) {
        MenuItemType entity =
                repository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "MenuItemType not found with id: " + id));
        return toDto(entity);
    }

    public List<MenuItemTypeDto> findAllMenuItemTypes() {
        List<MenuItemType> menuItemTypes = repository.findAll();
        return menuItemTypes.stream().map(this::toDto).collect(Collectors.toList());
    }

    // TBC if MenuItemType can be added by staff. if so add createMenuItemType method and
    // deleteMenuItemType

    private MenuItemTypeDto toDto(MenuItemType entity) {
        MenuItem menuItem = entity.getMenuItem();

        return new MenuItemTypeDto(
                entity.getMenuItemTypeId(),
                menuItem != null ? menuItem.getMenuItemId() : 0L,
                entity.getSize(),
                entity.getPrice(),
                entity.isAvailable());
    }
}
