package org.coffeeshop.purchaseorders.services;

import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.coffeeshop.exceptions.purchaseorderexceptions.InvalidOrderStatusTransition;
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
import org.coffeeshop.users.models.Staff;
import org.coffeeshop.users.repositories.CustomerRepository;
import org.coffeeshop.users.repositories.StaffRepository;
import org.springframework.stereotype.Service;
/**
 * Service layer for purchase order operations.
 * Handles order creation with price calculation, status updates,
 * and querying orders by ID or retrieving all orders.
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-23
 */
@Service
public class PurchaseOrderService {
    private final PurchaseOrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final StationRepository stationRepository;
    private final StaffRepository staffRepository;
    private final MenuItemTypeRepository menuItemTypeRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * Constructs the service with all required repositories.
     *
     * @param orderRepository       repository for purchase order entities
     * @param customerRepository    repository for customer entities
     * @param stationRepository     repository for station entities
     * @param staffRepository       repository for staff entities
     * @param menuItemTypeRepository repository for menu item type entities
     * @param orderItemRepository   repository for order item entities
     */
    public PurchaseOrderService(
            PurchaseOrderRepository orderRepository,
            CustomerRepository customerRepository,
            StationRepository stationRepository,
            StaffRepository staffRepository,
            MenuItemTypeRepository menuItemTypeRepository,
            OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.stationRepository = stationRepository;
        this.staffRepository = staffRepository;
        this.menuItemTypeRepository = menuItemTypeRepository;
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Retrieves a purchase order by its ID.
     *
     * @param id the purchase order ID
     * @return the order as a DTO
     * @throws jakarta.persistence.EntityNotFoundException if no order exists with the given ID
     */
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

    /**
     * Retrieves all purchase orders.
     *
     * @return a list of all orders as DTOs
     */
    public List<PurchaseOrderDto> findAllPurchaseOrders() {
        List<PurchaseOrder> orders = orderRepository.findAll();
        return orders.stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Creates a new purchase order. Validates the station, customer, and all menu item types
     * exist, calculates line totals and the overall total amount, persists the order and its items.
     *
     * @param orderDto the creation request containing customer, station, pickup time, and order items
     * @return the created order as a DTO
     * @throws jakarta.persistence.EntityNotFoundException if the station, customer, or any menu item type is not found
     * @throws IllegalArgumentException                    if the pickup time is outside the station's opening hours
     * @since 2026-04-23
     */
    public PurchaseOrderDto createOrder(CreatePurchaseOrderDto orderDto) {
        Station station =
                stationRepository
                        .findById(orderDto.stationId())
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "Station not found: " + orderDto.stationId()));

        validatePickupTimeForStation(station, orderDto.pickupTime());
        
        Customer customer =
                customerRepository
                        .findById(orderDto.customerId())
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "Customer not found: " + orderDto.customerId()));


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

        /**
         * Validates a requested pickup time against today's schedule for the given station.
         *
         * @param station the station handling the order
         * @param pickupTime the requested pickup time
         * @throws IllegalArgumentException if the station is closed, the pickup time is in the past,
         * or the pickup time is outside opening hours
         * @since 2026-04-23
         */
        private void validatePickupTimeForStation(Station station, LocalTime pickupTime) {
                DayOfWeek today = LocalDate.now().getDayOfWeek();

                if (today == DayOfWeek.SUNDAY && station.isClosedOnSunday()) {
                        throw new IllegalArgumentException(
                                        "Pickup time "
                                                        + pickupTime
                                                        + " is invalid for station "
                                                        + station.getName()
                                                        + ". Station is closed today.");
                }

                String hoursRange = today == DayOfWeek.SATURDAY
                                ? station.getSaturdayOpeningHours()
                                : station.getWeekdayOpeningHours();

                LocalTime[] hours = parseOpeningHours(hoursRange, station.getName());
                LocalTime openTime = hours[0];
                LocalTime closeTime = hours[1];
                LocalTime now = LocalTime.now().withSecond(0).withNano(0);

                if (pickupTime.isBefore(now)) {
                        throw new IllegalArgumentException(
                                        "Pickup time "
                                                        + pickupTime
                                                        + " is invalid for station "
                                                        + station.getName()
                                                        + ". Pickup time cannot be earlier than current time "
                                                        + now
                                                        + ".");
                }

                if (pickupTime.isBefore(openTime) || pickupTime.isAfter(closeTime)) {
                        throw new IllegalArgumentException(
                                        "Pickup time "
                                                        + pickupTime
                                                        + " is invalid for station "
                                                        + station.getName()
                                                        + ". Valid hours today: "
                                                        + hoursRange
                                                        + ".");
                }
        }

        /**
         * Parses opening hours in HH:mm-HH:mm format.
         *
         * @param hoursRange the opening-hours string
         * @param stationName station name for diagnostics
         * @return parsed opening and closing times
         * @throws IllegalArgumentException if the hours format is missing or invalid
         * @since 2026-04-23
         */
        private LocalTime[] parseOpeningHours(String hoursRange, String stationName) {
                if (hoursRange == null || !hoursRange.contains("-")) {
                        throw new IllegalArgumentException("Opening hours are invalid for station " + stationName);
                }

                String[] parts = hoursRange.split("-");
                if (parts.length != 2) {
                        throw new IllegalArgumentException("Opening hours are invalid for station " + stationName);
                }

                LocalTime openTime = LocalTime.parse(parts[0].trim());
                LocalTime closeTime = LocalTime.parse(parts[1].trim());
                return new LocalTime[] {openTime, closeTime};
}

    /**
     * Retrieves all purchase orders matching the given status.
     *
     * @param status the order status to filter by
     * @return a list of matching orders as DTOs
     */
    public List<PurchaseOrderDto> findByStatus(OrderStatus status) {
        List<PurchaseOrder> orders = orderRepository.findByOrderStatus(status);
        return orders.stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves all purchase orders belonging to the given customer.
     *
     * @param id the customer ID to filter by
     * @return a list of matching orders as DTOs
     */
    public List<PurchaseOrderDto> findByCustomerId(Long id) {
        List<PurchaseOrder> orders = orderRepository.findByCustomerCustomerId(id);
        return orders.stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves all purchase orders belonging to the given customer phone number.
     *
     * @param phoneNumber the customer phone number to filter by
     * @return a list of matching orders as DTOs
     */
     public List<PurchaseOrderDto> findByPhoneNumber(String phoneNumber) {
        Customer customer = customerRepository.findByCustomerPhoneNumber(phoneNumber);
        if (customer == null) {
            throw new EntityNotFoundException("Customer not found with phone number: " + phoneNumber);
        }

        System.out.println("Found customer for phone number(Service) " + phoneNumber + ": " + customer); // Debug log
        List<PurchaseOrder> orders = orderRepository.findByCustomerCustomerId(customer.getId());

        System.out.println("Found " + orders.size() + " orders for customer ID " + customer.getId()); // Debug log
      
        return orders.stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves all purchase orders belonging to the given staff member.
     *
     * @param id the staff ID to filter by
     * @return a list of matching orders as DTOs
     */
    public List<PurchaseOrderDto> findByStaffId(Long id) {
        List<PurchaseOrder> orders = orderRepository.findByStaffStaffId(id);
        return orders.stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Updates the archive flag of an existing purchase order.
     *
     * @param orderId    the purchase order ID
     * @param isArchived the new archive flag value
     * @return the updated order as a DTO
     * @throws IllegalArgumentException if the order ID is null
     * @throws jakarta.persistence.EntityNotFoundException if no order exists with the given ID
     */
    public PurchaseOrderDto updateArchiveFlag(Long orderId, boolean isArchived) {
        if(orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        PurchaseOrder existing =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "PurchaseOrder not found: " + orderId));

        PurchaseOrder updated =
                new PurchaseOrder(
                        existing.getPurchaseOrderId(),
                        existing.getCustomer(),
                        existing.getStation(),
                        existing.getStaff(),
                        existing.getOrderDate(),
                        existing.getPickupTime(),
                        existing.getOrderStatus(),
                        existing.getTotalAmount(),
                        isArchived);

        PurchaseOrder saved = orderRepository.save(updated);
        return toDto(saved);
    }
    
    /**
     * Updates the status of an existing purchase order.
     * Throws an exception if the order is already CANCELLED, as cancelled orders
     * cannot transition to any other status.
     *
     * @param orderId  the purchase order ID
     * @param staffId  the staff ID preparing the order
     * @param dto the request containing the new order status
     * @return the updated order as a DTO
     * @throws jakarta.persistence.EntityNotFoundException         if no order exists with the given ID
     * @throws InvalidOrderStatusTransition                        if the order is CANCELLED and a status change is attempted
     */
    public PurchaseOrderDto updateOrderStatus(Long orderId, Long staffId, UpdateOrderStatusDto dto) {
        PurchaseOrder existing =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "PurchaseOrder not found: " + orderId));

        if (existing.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStatusTransition("Cannot transition order " + orderId + " from CANCELLED to " + dto.orderStatus());
        }
        
        Staff staff = staffRepository
                        .findById(staffId)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "Staff member not found: " + staffId));
        
        PurchaseOrder updated =
                new PurchaseOrder(
                        existing.getPurchaseOrderId(),
                        existing.getCustomer(),
                        existing.getStation(),
                        staff,
                        existing.getOrderDate(),
                        existing.getPickupTime(),
                        dto.orderStatus(),
                        existing.getTotalAmount(),
                        existing.getIsArchived());

        PurchaseOrder saved = orderRepository.save(updated);
        return toDto(saved);
    }
    
    /**
     * Cancels an order by setting its status to CANCELLED.
     * Used by the auto-cancellation scheduler when the 15-minute collection window expires.
     * Unlike {@link #updateOrderStatus}, this method does not require a staff ID.
     * In case no member of staff was assigned to the order.
     *
     * @param orderId the purchase order ID
     * @throws jakarta.persistence.EntityNotFoundException if no order exists with the given ID
     */
    public void cancelOrder(Long orderId) {
        PurchaseOrder existing =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "PurchaseOrder not found: " + orderId));
        
        if (existing.getOrderStatus() == OrderStatus.COLLECTED || existing.getOrderStatus() == OrderStatus.CANCELLED) {
                return;
        }
        
        PurchaseOrder updated =
                new PurchaseOrder(
                        existing.getPurchaseOrderId(),
                        existing.getCustomer(),
                        existing.getStation(),
                        existing.getStaff(),
                        existing.getOrderDate(),
                        existing.getPickupTime(),
                        OrderStatus.CANCELLED,
                        existing.getTotalAmount(),
                        existing.getIsArchived());
        orderRepository.save(updated);
    }

    /**
     * Converts a PurchaseOrder entity to a PurchaseOrderDto.
     * Handles null customer or station references by defaulting to 0L.
     *
     * @param order the entity to convert
     * @return the corresponding DTO
     */
    private PurchaseOrderDto toDto(PurchaseOrder order) {
        Customer customer = order.getCustomer();
        Station station = order.getStation();
        Staff staff = order.getStaff();

        return new PurchaseOrderDto(
                order.getPurchaseOrderId(),
                customer != null ? customer.getId() : 0L,
                station != null ? station.getId() : 0L,
                staff != null ? staff.getStaffId() : 0L,
                order.getIsArchived(),
                order.getOrderDate(),
                order.getPickupTime(),
                order.getOrderStatus(),
                order.getTotalAmount());
    }
}
