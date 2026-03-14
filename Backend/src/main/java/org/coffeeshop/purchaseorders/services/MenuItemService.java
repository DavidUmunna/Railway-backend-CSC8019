package org.coffeeshop.purchaseorders.services;

import org.coffeeshop.purchaseorders.dtos.MenuItemResponseDto;
import org.coffeeshop.purchaseorders.models.MenuItem;
import org.coffeeshop.purchaseorders.repositories.MenuItemRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service 
public class MenuItemService {
    private final MenuItemRepository repository;

    public MenuItemService(MenuItemRepository repository) {
        this.repository = repository;
    }


    public MenuItemResponseDto getById(Long id) {
        MenuItem entity = repository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("MenuItem not found with id: " + id));
        return toDto(entity);
    }

    private MenuItemResponseDto toDto(MenuItem entity) {
        return new MenuItemResponseDto(
            entity.getMenuItemId(),
            entity.getName(),
            entity.getDescription(),
            entity.isAvailable()
        );
    }
}