package org.coffeeshop.stations.models;

import jakarta.persistence.*;
// import org.coffeeshop.purchaseorder.model.PurchaseOrder;

// import java.util.ArrayList;
import java.util.List;

import org.coffeeshop.purchaseorders.models.PurchaseOrder;

@Entity
@Table(name = "station")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "station_id")
    private int id;

    @Column(name = "station_name")
    private String name;

    @OneToMany(mappedBy = "station")
    private List<PurchaseOrder> orders; // TBC should initialize here as well? 

    public Station() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<PurchaseOrder> getOrders() {
        return orders;
    }

    public void setName(String name) {
        this.name = name;
    }


}
