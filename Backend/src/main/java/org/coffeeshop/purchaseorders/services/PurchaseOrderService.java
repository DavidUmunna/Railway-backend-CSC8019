package org.coffeeshop.purchaseorders.services;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.coffeeshop.purchaseorders.dtos.CreateOrderItemDto;
import org.coffeeshop.purchaseorders.dtos.CreatePurchaseOrderDto;
import org.coffeeshop.purchaseorders.dtos.PurchaseOrderDto;
import org.coffeeshop.purchaseorders.dtos.UpdateOrderStatusDto;
import org.coffeeshop.purchaseorders.models.MenuItemType;
import org.coffeeshop.purchaseorders.models.OrderItem;
import org.coffeeshop.purchaseorders.models.OrderStatus;
import org.coffeeshop.purchaseorders.models.PurchaseOrder;
import org.coffeeshop.purchaseorders.repositories.MenuItemTypeRepository;
import org.coffeeshop.purchaseorders.repositories.OrderItemRepository;
import org.coffeeshop.purchaseorders.repositories.PurchaseOrderRepository;
import org.coffeeshop.stations.models.Station;
import org.coffeeshop.stations.repositories.StationRepository;
import org.coffeeshop.users.models.Customer;
import org.coffeeshop.users.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderService {
    private final PurchaseOrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final StationRepository stationRepository;
    private final MenuItemTypeRepository menuItemTypeRepository;
    private final OrderItemRepository orderItemRepository;

    public PurchaseOrderService(
            PurchaseOrderRepository orderRepository,
            CustomerRepository customerRepository,
            StationRepository stationRepository,
            MenuItemTypeRepository menuItemTypeRepository,
            OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.stationRepository = stationRepository;
        this.menuItemTypeRepository = menuItemTypeRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public PurchaseOrderDto getById(Long id) {
        PurchaseOrder entity =
                orderRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "PurchaseOrder not found with id: " + id));
        return toDto(entity);
    }

    public List<PurchaseOrderDto> findAllPurchaseOrders() {
        List<PurchaseOrder> orders = orderRepository.findAll();
        return orders.stream().map(this::toDto).collect(Collectors.toList());
    }

    public PurchaseOrderDto createOrder(CreatePurchaseOrderDto orderDto) {
        Customer customer =
                customerRepository
                        .findById(orderDto.customerId())
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "Customer not found: " + orderDto.customerId()));

        Station station =
                stationRepository
                        .getStationById(orderDto.stationId())
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "Station not found: " + orderDto.stationId()));

        double totalAmount = 0.0;
        List<OrderItem> items = new ArrayList<>();

        for (CreateOrderItemDto itemDto : orderDto.orderItems()) {
            MenuItemType menuItemType =
                    menuItemTypeRepository
                            .findById(itemDto.menuItemTypeId())
                            .orElseThrow(
                                    () ->
                                            new EntityNotFoundException(
                                                    "MenuItemType not found: "
                                                            + itemDto.menuItemTypeId()));

            double unitPrice = menuItemType.getPrice();
            double lineTotal = unitPrice * itemDto.quantity();
            totalAmount += lineTotal;

            items.add(new OrderItem(menuItemType, itemDto.quantity(), unitPrice, lineTotal));
        }

        PurchaseOrder order =
                new PurchaseOrder(
                        customer,
                        station,
                        LocalDate.now(),
                        orderDto.pickupTime(),
                        OrderStatus.ACCEPTED,
                        totalAmount);
        PurchaseOrder savedOrder = orderRepository.save(order);

        for (OrderItem item : items) {
            OrderItem itemWithOrder =
                    new OrderItem(
                            savedOrder,
                            item.getMenuItemType(),
                            item.getQuantity(),
                            item.getUnitPrice(),
                            item.getLineTotal());
            orderItemRepository.save(itemWithOrder);
        }

        return toDto(savedOrder);
    }

    public PurchaseOrderDto updateOrderStatus(Long id, UpdateOrderStatusDto dto) {
        PurchaseOrder existing =
                orderRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "PurchaseOrder not found: " + id));

        PurchaseOrder updated =
                new PurchaseOrder(
                        existing.getPurchaseOrderId(),
                        existing.getCustomer(),
                        existing.getStation(),
                        existing.getOrderDate(),
                        existing.getPickupTime(),
                        dto.orderStatus(),
                        existing.getTotalAmount());

        PurchaseOrder saved = orderRepository.save(updated);
        return toDto(saved);
    }

    private PurchaseOrderDto toDto(PurchaseOrder order) {
        Customer cust = order.getCustomer();
        Station station = order.getStation();

        return new PurchaseOrderDto(
                order.getPurchaseOrderId(),
                cust != null ? cust.getId() : 0L,
                station != null ? station.getId() : 0L,
                order.getOrderDate(),
                order.getPickupTime(),
                order.getOrderStatus(),
                order.getTotalAmount());
    }
}
