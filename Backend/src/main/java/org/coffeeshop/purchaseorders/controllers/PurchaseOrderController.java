package org.coffeeshop.purchaseorders.controllers;

import jakarta.validation.Valid;
import java.util.List;
import org.coffeeshop.purchaseorders.dtos.CreatePurchaseOrderDto;
import org.coffeeshop.purchaseorders.dtos.PurchaseOrderDto;
import org.coffeeshop.purchaseorders.dtos.UpdateOrderStatusDto;
import org.coffeeshop.purchaseorders.services.PurchaseOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class PurchaseOrderController {
    private final PurchaseOrderService service;

    public PurchaseOrderController(PurchaseOrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderDto> createOrder(
            @Valid @RequestBody CreatePurchaseOrderDto dto) {
        PurchaseOrderDto created = service.createOrder(dto);

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderDto> getById(@PathVariable Long id) {
        PurchaseOrderDto dto = service.getById(id);

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderDto>> findAll() {
        List<PurchaseOrderDto> orders = service.findAllPurchaseOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PurchaseOrderDto> updateStatus(
            @PathVariable Long id, @Valid @RequestBody UpdateOrderStatusDto status) {
        PurchaseOrderDto updated = service.updateOrderStatus(id, status);

        return ResponseEntity.ok(updated);
    }
}
