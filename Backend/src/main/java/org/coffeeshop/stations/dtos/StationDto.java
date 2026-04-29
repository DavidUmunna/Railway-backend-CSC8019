package org.coffeeshop.stations.dtos;

public record StationDto(
        Long id,
        String name,
        String weekdayOpeningHours,
        String saturdayOpeningHours,
        boolean closedOnSunday
) {}