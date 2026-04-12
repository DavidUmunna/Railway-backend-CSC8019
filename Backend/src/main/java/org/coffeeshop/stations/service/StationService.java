package org.coffeeshop.stations.service;

import org.coffeeshop.stations.dtos.StationDtos;
import org.coffeeshop.stations.models.Station;
import org.coffeeshop.stations.repositories.StationRepository;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public boolean isOpen(int stationId, LocalDateTime dateTime) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Station not found"));

        DayOfWeek day = dateTime.getDayOfWeek();
        LocalTime time = dateTime.toLocalTime();

        if (day == DayOfWeek.SUNDAY && station.isClosedOnSunday()) {
            return false;
        }

        String hours = (day == DayOfWeek.SATURDAY)
                ? station.getSaturdayOpeningHours()
                : station.getWeekdayOpeningHours();

        if (hours == null || !hours.contains("-")) return false;

        String[] parts = hours.split("-");
        LocalTime open = LocalTime.parse(parts[0]);
        LocalTime close = LocalTime.parse(parts[1]);

        return !time.isBefore(open) && !time.isAfter(close);
    }

    public StationDtos convertToDto(Station station) {
        StationDtos dto = new StationDtos();
        dto.setId(station.getId());
        dto.setName(station.getName());
        dto.setWeekdayOpeningHours(station.getWeekdayOpeningHours());
        dto.setSaturdayOpeningHours(station.getSaturdayOpeningHours());
        dto.setClosedOnSunday(station.isClosedOnSunday());
        return dto;
    }
}