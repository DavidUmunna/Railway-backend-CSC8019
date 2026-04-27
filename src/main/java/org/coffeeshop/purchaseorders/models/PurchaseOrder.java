package org.coffeeshop.purchaseorders.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import org.coffeeshop.stations.models.Station;
import org.coffeeshop.users.models.Customer;
import org.coffeeshop.users.models.Staff;

/**
 * JPA entity representing a customer purchase order.
 * Each order is linked to a customer and a station,
 * and tracks the order date, pickup time, status, and total amount.
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-21
 */
@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {

    /** Auto-generated primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    /** The customer who placed the order. */
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    /** The station where the order will be prepared. */
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    /** The date the order was placed. */
    @Column(name = "order_date")
    private LocalDate orderDate;

    /** The requested pickup time for the order. */
    @Column(name = "pickup_time")
    private LocalTime pickupTime;

    /** The current lifecycle status of the order. */
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    /** The total monetary amount for the order. */
    @Column(name = "total_amount")
    private double totalAmount;

    /** No-arg constructor required by JPA. */
    protected PurchaseOrder() {}

    /**
     * Full constructor including the order ID.
     *
     * @param orderId      the primary key (null for new entities)
     * @param customer     the customer placing the order
     * @param station      the station preparing the order
     * @param staff        the staff memeber preparing the order
     * @param orderDate    the date the order was placed
     * @param pickupTime   the requested pickup time
     * @param orderStatus  the initial status
     * @param totalAmount  the calculated total amount
     */
    public PurchaseOrder(
            Long orderId,
            Customer customer,
            Station station,
            Staff staff,
            LocalDate orderDate,
            LocalTime pickupTime,
            OrderStatus orderStatus,
            double totalAmount) {
        this.orderId = orderId;
        this.customer = customer;
        this.staff = staff;
        this.station = station;
        this.orderDate = orderDate;
        this.pickupTime = pickupTime;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
    }

    /**
     * Constructor without ID for creating new entities.
     *
     * @param customer     the customer placing the order
     * @param station      the station preparing the order
     * @param orderDate    the date the order was placed
     * @param pickupTime   the requested pickup time
     * @param orderStatus  the initial status
     * @param totalAmount  the calculated total amount
     */
    public PurchaseOrder(
            Customer customer,
            Station station,
            LocalDate orderDate,
            LocalTime pickupTime,
            OrderStatus orderStatus,
            double totalAmount) {
        this(null, customer, station, null, orderDate, pickupTime, orderStatus, totalAmount);
    }

    public Long getPurchaseOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Station getStation() {
        return station;
    }

    public Staff getStaff() {
        return staff;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public LocalTime getPickupTime() {
        return pickupTime;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
