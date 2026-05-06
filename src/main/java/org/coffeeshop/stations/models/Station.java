package org.coffeeshop.stations.models;

import jakarta.persistence.*;

/**
 * Entity representing a coffee shop station with opening hours.
 *
 * @author Christy Zheng
 * @version 1.0
 * @since 2026-04-17
 * @modifiedby Kulagina Tatiana
 * @since 2026-04-28
 */
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

    /** No-arg constructor required by JPA. */
    public Station() {}

    /**
     * Creates a new Station with the given name and opening hours.
     *
     * @param name the station name
     * @param weekdayOpeningHours weekday hours in HH:mm-HH:mm format
     * @param saturdayOpeningHours saturday hours in HH:mm-HH:mm format
     * @param closedOnSunday whether the station is closed on Sundays
     */
    public Station(String name, String weekdayOpeningHours, String saturdayOpeningHours, boolean closedOnSunday) {
        this.name = name;
        this.weekdayOpeningHours = weekdayOpeningHours;
        this.saturdayOpeningHours = saturdayOpeningHours;
        this.closedOnSunday = closedOnSunday;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getWeekdayOpeningHours() { return weekdayOpeningHours; }

    public String getSaturdayOpeningHours() { return saturdayOpeningHours; }

    public boolean isClosedOnSunday() { return closedOnSunday; }

    /**
     * Updates this station's schedule fields.
     *
     * @param weekdayHours  weekday hours in HH:mm-HH:mm format
     * @param saturdayHours saturday hours in HH:mm-HH:mm format
     * @param closedOnSunday whether the station is closed on Sundays
     */
    public void updateSchedule(String weekdayHours, String saturdayHours, boolean closedOnSunday) {
        this.weekdayOpeningHours = weekdayHours;
        this.saturdayOpeningHours = saturdayHours;
        this.closedOnSunday = closedOnSunday;
    }
}