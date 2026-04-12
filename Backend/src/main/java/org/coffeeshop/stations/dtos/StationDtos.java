package org.coffeeshop.stations.dtos;

public class StationDtos {
    private int id;
    private String name;
    private String weekdayOpeningHours;
    private String saturdayOpeningHours;
    private boolean closedOnSunday;

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getWeekdayOpeningHours() {return weekdayOpeningHours;}
    public void setWeekdayOpeningHours(String weekdayOpeningHours) {this.weekdayOpeningHours = weekdayOpeningHours;}
    public String getSaturdayOpeningHours() {return saturdayOpeningHours;}
    public void setSaturdayOpeningHours(String saturdayOpeningHours) {this.saturdayOpeningHours = saturdayOpeningHours;}
    public boolean isClosedOnSunday() {return closedOnSunday;}
    public void setClosedOnSunday(boolean closedOnSunday) {this.closedOnSunday = closedOnSunday;}
}
