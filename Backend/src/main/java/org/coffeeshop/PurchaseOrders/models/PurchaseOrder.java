package org.coffeeshop.purchaseorders.models;

import java.time.LocalDate;
import java.time.LocalTime;

import org.coffeeshop.stations.models.Station;
import org.coffeeshop.users.models.Customer;

import jakarta.persistence.*;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="station_id")
    private Station station;

    @Column(name="order_date")
    private LocalDate orderDate;

    @Column(name="pickup_time")
    private LocalTime pickupTime;

    @Enumerated(EnumType.STRING)
    @Column(name="order_status")
    private OrderStatus orderStatus; 

    @Column(name="total_amount")
    private double totalAmount;
    
    public PurchaseOrder() {} 

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
    
    public void setPickUpTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setStation(Station station) {
        this.station = station;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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