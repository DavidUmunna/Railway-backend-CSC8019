package org.coffeeshop.stations.services;

import org.coffeeshop.stations.dtos.StationDto;
import org.coffeeshop.stations.models.Station;
import org.coffeeshop.stations.repositories.StationRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<StationDto> getAllStations() {
        return stationRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public StationDto getStationById(Long stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Station not found!"));
        return convertToDto(station);
    }

    public boolean isOpen(Long stationId, LocalDateTime dateTime) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Station not found!"));

        DayOfWeek day = dateTime.getDayOfWeek();
        LocalTime time = dateTime.toLocalTime();

        if (day == DayOfWeek.SUNDAY && station.isClosedOnSunday()) return false;

        String hours = (day == DayOfWeek.SATURDAY)
                ? station.getSaturdayOpeningHours()
                : station.getWeekdayOpeningHours();

        if (hours == null || !hours.contains("-")) return false;

        String[] parts = hours.split("-");
        LocalTime open = LocalTime.parse(parts[0].trim());
        LocalTime close = LocalTime.parse(parts[1].trim());

        return !time.isBefore(open) && !time.isAfter(close);
    }

    public void validatePickupTime(Long stationId, LocalDateTime pickupTime) {
        if (!isOpen(stationId, pickupTime)) {
            throw new IllegalArgumentException("Invalid pickup time!");
        }
    }

    public StationDto updateOpeningHours(Long stationId, String weekday, String saturday, boolean sunday) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Station not found!"));

        station.updateSchedule(weekday, saturday);

        return convertToDto(stationRepository.save(station));
    }

    public StationDto convertToDto(Station station) {
        return new StationDto(
                station.getId(),
                station.getName(),
                station.getWeekdayOpeningHours(),
                station.getSaturdayOpeningHours(),
                station.isClosedOnSunday()
        );
    }
}