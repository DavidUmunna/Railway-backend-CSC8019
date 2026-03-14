package org.coffeeshop.users.models;

import java.util.List;

import org.coffeeshop.purchaseorders.models.PurchaseOrder;

import jakarta.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int id;

    @Column(name = "customer_name")
    private String name;
    
    @Column(name = "customer_phone")
    private String phone;

    @OneToMany(mappedBy = "customer")
    private List<PurchaseOrder> orders;  // TBC should initialize here as well? 
    
    public Customer() {}

    public void setName(String name) {
        this.name = name;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }
}
