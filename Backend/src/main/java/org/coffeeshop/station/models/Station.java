/*package org.coffeeshop.station.model;

import jakarta.persistence.*;
import org.coffeeshop.purchaseorder.model.PurchaseOrder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stations")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;


    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<PurchaseOrder> orders = new ArrayList<>();

    // === getters/setters ===

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<PurchaseOrder> getOrders() {
        return orders;
    }


}*/
