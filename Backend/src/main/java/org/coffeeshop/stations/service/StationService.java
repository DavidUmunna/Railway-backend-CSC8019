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

    // Used by controller to return station data to frontend
    public List<StationDto> getAllStations() {
        return stationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public StationDto getStationById(int stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Station not found"));
        return convertToDto(station);
    }
    /*
     * Check if a station is open at a given time
     * Could be reused when validating customer pickup time
     */

    public boolean isOpen(int stationId, LocalDateTime dateTime) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Station not found"));

        DayOfWeek day = dateTime.getDayOfWeek();
        LocalTime time = dateTime.toLocalTime();

        // Sunday rule from project brief
        if (day == DayOfWeek.SUNDAY && station.isClosedOnSunday()) {
            return false;
        }

        // Saturday has different opening hours
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

    public StationDto convertToDto(Station station) {
        StationDto dto = new StationDto();
        dto.setId(station.getId());
        dto.setName(station.getName());
        dto.setWeekdayOpeningHours(station.getWeekdayOpeningHours());
        dto.setSaturdayOpeningHours(station.getSaturdayOpeningHours());
        dto.setClosedOnSunday(station.isClosedOnSunday());
        return dto;
    }
    /*
     * TBC:
     * - validate pickup time when creating orders
     * - allow updating opening hours
     */

}