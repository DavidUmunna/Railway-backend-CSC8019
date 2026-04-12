package org.coffeeshop.stations.models;

import jakarta.persistence.*;
import java.util.List;
import org.coffeeshop.purchaseorders.models.PurchaseOrder;

@Entity
@Table(name = "station")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "station_id")
    private int id;

    @Column(name = "station_name",nullable = false)
    private String name;

    //opening hours
    private String weekdayOpeningHours;
    private String saturdayOpeningHours;
    private boolean closedOnSunday = true;

    @OneToMany(mappedBy = "station")
    private List<PurchaseOrder> orders; // TBC should initialize here as well? 

    public Station() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWeekdayOpeningHours() {return weekdayOpeningHours;}
    public void setWeekdayOpeningHours(String hours) { this.weekdayOpeningHours = hours; }

    public String getSaturdayOpeningHours() {return saturdayOpeningHours;}
    public void setSaturdayOpeningHours(String hours) { this.saturdayOpeningHours = hours; }

    public boolean isClosedOnSunday() { return closedOnSunday; }
    public void setClosedOnSunday(boolean closedOnSunday) { this.closedOnSunday = closedOnSunday; }

    public List<PurchaseOrder> getOrders() {
        return orders;
    }

    public void setName(String name) {
        this.name = name;
    }

}
