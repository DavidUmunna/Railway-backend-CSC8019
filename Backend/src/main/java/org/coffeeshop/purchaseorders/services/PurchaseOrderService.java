package org.coffeeshop.purchaseorders.services;

import org.coffeeshop.purchaseorders.dtos.PurchaseOrderDto;
import org.coffeeshop.purchaseorders.models.PurchaseOrder;
import org.coffeeshop.purchaseorders.repositories.PurchaseOrderRepository;
import org.coffeeshop.stations.models.Station;
import org.coffeeshop.users.models.Customer;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service 
public class PurchaseOrderService {
    private final PurchaseOrderRepository repository;

    public PurchaseOrderService(PurchaseOrderRepository repository) {
        this.repository = repository;
    }


    public PurchaseOrderDto getById(Long id) {
        PurchaseOrder entity = repository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("PurchaseOrder not found with id: " + id));
        return toDto(entity);
    }

    private PurchaseOrderDto toDto(PurchaseOrder entity) {
        Customer cust = entity.getCustomer();
        Station station = entity.getStation();

        return new PurchaseOrderDto(
            entity.getPurchaseOrderId(),
            cust != null ? cust.getId() : 0L,
            station != null ? station.getId() : 0L,
            entity.getOrderDate(),
            entity.getPickupTime(),
            entity.getOrderStatus(),
            entity.getTotalAmount()
        );
    }
}
