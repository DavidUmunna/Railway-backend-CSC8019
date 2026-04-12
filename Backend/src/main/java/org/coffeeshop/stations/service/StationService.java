package org.coffeeshop.stations.service;

import org.coffeeshop.stations.dtos.StationDto;
import org.coffeeshop.stations.models.Station;
import org.coffeeshop.stations.repositories.StationRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    // Get all stations
    public List<StationDto> getAllStations() {
        return stationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get station by id
    public StationDto getStationById(int stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Station not found"));
        return convertToDto(station);
    }

    /*
     * Check if station is open at given time
     */
    public boolean isOpen(int stationId, LocalDateTime dateTime) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Station not found!"));

        DayOfWeek day = dateTime.getDayOfWeek();
        LocalTime time = dateTime.toLocalTime();

        // Sunday rule
        if (day == DayOfWeek.SUNDAY && station.isClosedOnSunday()) {
            return false;
        }

        // Choose correct hours
        String hours = (day == DayOfWeek.SATURDAY)
                ? station.getSaturdayOpeningHours()
                : station.getWeekdayOpeningHours();

        if (hours == null || !hours.contains("-")) {
            return false;
        }

        String[] parts = hours.split("-");
        LocalTime open = LocalTime.parse(parts[0].trim());
        LocalTime close = LocalTime.parse(parts[1].trim());

        return !time.isBefore(open) && !time.isAfter(close);
    }

    /*
     * Validate pickup time when creating an order
     * Throws error if time is outside opening hours
     */
    public void validatePickupTime(int stationId, LocalDateTime pickupTime) {
        if (!isOpen(stationId, pickupTime)) {
            throw new IllegalArgumentException("Invalid pickup time!");
        }
    }

    /*
     * Allow owner revise opening hours
     */
    public StationDto updateOpeningHours(int stationId,
                                         String weekdayOpeningHours,
                                         String saturdayOpeningHours,
                                         boolean closedOnSunday) {

        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Station not found!"));

        station.setWeekdayOpeningHours(weekdayOpeningHours);
        station.setSaturdayOpeningHours(saturdayOpeningHours);
        station.setClosedOnSunday(closedOnSunday);

        Station updated = stationRepository.save(station);
        return convertToDto(updated);
    }

    // Convert entity → DTO
    public StationDto convertToDto(Station station) {
        StationDto dto = new StationDto();
        dto.setId(station.getId());
        dto.setName(station.getName());
        dto.setWeekdayOpeningHours(station.getWeekdayOpeningHours());
        dto.setSaturdayOpeningHours(station.getSaturdayOpeningHours());
        dto.setClosedOnSunday(station.isClosedOnSunday());
        return dto;
    }
}