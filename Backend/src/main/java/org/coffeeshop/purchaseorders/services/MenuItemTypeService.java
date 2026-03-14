package org.coffeeshop.purchaseorders.services;

import org.coffeeshop.purchaseorders.dtos.MenuItemTypeResponseDto;
import org.coffeeshop.purchaseorders.models.MenuItem;
import org.coffeeshop.purchaseorders.models.MenuItemType;
import org.coffeeshop.purchaseorders.repositories.MenuItemTypeRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service 
public class MenuItemTypeService {
    private final MenuItemTypeRepository repository;

    public MenuItemTypeService(MenuItemTypeRepository repository) {
        this.repository = repository;
    }


    public MenuItemTypeResponseDto getById(Long id) {
        MenuItemType entity = repository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("MenuItemType not found with id: " + id));
        return toDto(entity);
    }

    private MenuItemTypeResponseDto toDto(MenuItemType entity) {
        MenuItem menuItem = entity.getMenuItem();

        return new MenuItemTypeResponseDto(
            entity.getMenuItemTypeId(),
            menuItem != null ? menuItem.getMenuItemId() : 0L,
            entity.getSize(),
            entity.getPrice(),
            entity.isAvailable()
        );
    }
}