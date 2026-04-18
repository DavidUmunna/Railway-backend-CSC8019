package org.coffeeshop.purchaseorders.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import org.coffeeshop.stations.models.Station;
import org.coffeeshop.users.models.Customer;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "pickup_time")
    private LocalTime pickupTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "total_amount")
    private double totalAmount;

    protected PurchaseOrder() {}

    public PurchaseOrder(
            Long orderId,
            Customer customer,
            Station station,
            LocalDate orderDate,
            LocalTime pickupTime,
            OrderStatus orderStatus,
            double totalAmount) {
        this.orderId = orderId;
        this.customer = customer;
        this.station = station;
        this.orderDate = orderDate;
        this.pickupTime = pickupTime;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
    }

    public PurchaseOrder(
            Customer customer,
            Station station,
            LocalDate orderDate,
            LocalTime pickupTime,
            OrderStatus orderStatus,
            double totalAmount) {
        this(null, customer, station, orderDate, pickupTime, orderStatus, totalAmount);
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
