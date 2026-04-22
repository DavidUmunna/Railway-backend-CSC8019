package org.coffeeshop.purchaseorders.controllers;

import jakarta.validation.Valid;

import java.util.List;
import org.coffeeshop.purchaseorders.dtos.CreatePurchaseOrderDto;
import org.coffeeshop.purchaseorders.dtos.PurchaseOrderDto;
import org.coffeeshop.purchaseorders.dtos.UpdateOrderStatusDto;
import org.coffeeshop.purchaseorders.models.OrderStatus;
import org.coffeeshop.purchaseorders.services.PurchaseOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing purchase orders.
 * Provides endpoints for creating orders, retrieving orders by ID or as a list,
 * and updating order status.
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-21
 */
@RestController
@RequestMapping("/api/v1/orders")
public class PurchaseOrderController {
    private final PurchaseOrderService service;

    /**
     * Constructs the controller with the given purchase order service.
     *
     * @param service the purchase order service handling business logic
     */
    public PurchaseOrderController(PurchaseOrderService service) {
        this.service = service;
    }

    /**
     * Creates a new purchase order.
     *
     * @param dto the validated request body containing order details
     * @return 201 Created with the created order DTO
     */
    @PostMapping
    public ResponseEntity<PurchaseOrderDto> createOrder(
            @Valid @RequestBody CreatePurchaseOrderDto dto) {
        PurchaseOrderDto created = service.createOrder(dto);

        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Retrieves a purchase order by its ID.
     *
     * @param id the path variable specifying the order ID
     * @return 200 OK with the order DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderDto> getById(@PathVariable("id") Long id) {
        PurchaseOrderDto dto = service.getById(id);

        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves all purchase orders.
     *
     * @return 200 OK with a list of all order DTOs
     */
    @GetMapping
    public ResponseEntity<List<PurchaseOrderDto>> findAll() {
        List<PurchaseOrderDto> orders = service.findAllPurchaseOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Updates the status of an existing purchase order.
     *
     * @param orderId     the path variable specifying the order ID
     * @param staffId     the path variable specifying the staff ID preparing the order
     * @param status the validated request body containing the new status
     * @return 200 OK with the updated order DTO
     */
    @PutMapping("/{orderId}/{staffId}/status")
    public ResponseEntity<PurchaseOrderDto> updateStatus(
            @PathVariable("orderId") Long orderId, 
            @PathVariable("staffId") Long staffId, 
            @Valid @RequestBody UpdateOrderStatusDto status) {
        PurchaseOrderDto updated = service.updateOrderStatus(orderId, staffId, status);

        return ResponseEntity.ok(updated);
    }

    /**
     * Retrieves all purchase orders matching the given status.
     *
     * @param status the query parameter specifying the order status to filter by
     * @return 200 OK with a list of matching order DTOs
     */
    @GetMapping(params = "status")
    public ResponseEntity<List<PurchaseOrderDto>> findByStatus(@RequestParam("status") OrderStatus status) {
        List<PurchaseOrderDto> orders = service.findByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves all purchase orders belonging to the given customer.
     *
     * @param customerId the query parameter specifying the customer ID to filter by
     * @return 200 OK with a list of matching order DTOs
     */
    @GetMapping(params = "customerId")
    public ResponseEntity<List<PurchaseOrderDto>> findByCustomerId(@RequestParam("customerId") Long customerId) {
        List<PurchaseOrderDto> orders = service.findByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves all purchase orders belonging to the given staff.
     *
     * @param staffId the query parameter specifying the staff ID to filter by
     * @return 200 OK with a list of matching order DTOs
     */
    @GetMapping(params = "staffId")
    public ResponseEntity<List<PurchaseOrderDto>> findByStaffId(@RequestParam("staffId") Long staffId) {
        List<PurchaseOrderDto> orders = service.findByStaffId(staffId);
        return ResponseEntity.ok(orders);
    }
}
