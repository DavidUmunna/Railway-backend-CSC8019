package org.coffeeshop.purchaseorders.controllers;

import java.util.List;
import org.coffeeshop.purchaseorders.dtos.MenuItemDto;
import org.coffeeshop.purchaseorders.services.MenuItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/menu-items")
public class MenuItemController {
    private final MenuItemService service;

    public MenuItemController(MenuItemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MenuItemDto>> findAll() {
        List<MenuItemDto> items = service.findAllMenuItems();

        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDto> getById(@PathVariable Long id) {
        MenuItemDto dto = service.getById(id);

        return ResponseEntity.ok(dto);
    }
}
