package org.coffeeshop.stations.models;

import jakarta.persistence.*;

@Entity
@Table(name = "station")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "station_id")
    private Long id;

    @Column(name = "station_name", nullable = false)
    private String name;

    @Column(name="weekday_opening_hours")
    private String weekdayOpeningHours;
    @Column(name="saturday_opening_hours")
    private String saturdayOpeningHours;

    @Column(name="closed_on_sunday")
    private boolean closedOnSunday;

    public Station() {}

    public Station(String name, String weekdayOpeningHours, String saturdayOpeningHours, boolean closedOnSunday) {
        this.name = name;
        this.weekdayOpeningHours = weekdayOpeningHours;
        this.saturdayOpeningHours = saturdayOpeningHours;
        this.closedOnSunday = closedOnSunday;
    }

  
       

    public void updateSchedule(String weekday, String saturday) {
        this.weekdayOpeningHours = weekday;
        this.saturdayOpeningHours = saturday;
    }

    public Long getId() { return id; }

    public String getName() { return name; }


    public String getWeekdayOpeningHours() { return weekdayOpeningHours; }

    public String getSaturdayOpeningHours() { return saturdayOpeningHours; }


    public boolean isClosedOnSunday() {
        return closedOnSunday; }

}