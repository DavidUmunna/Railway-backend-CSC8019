package org.coffeeshop.stations.dtos;

public record StationDto(
        int id,
        String name,
        String weekdayOpeningHours,
        String saturdayOpeningHours,
        boolean closedOnSunday
) {}