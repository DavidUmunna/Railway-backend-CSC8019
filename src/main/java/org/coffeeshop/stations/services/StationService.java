package org.coffeeshop.stations.services;

import org.coffeeshop.stations.dtos.StationDto;
import org.coffeeshop.stations.dtos.UpdateStationDto;
import org.coffeeshop.stations.models.Station;
import org.coffeeshop.stations.repositories.StationRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalTime;
import java.util.List;

/**
 * Service for managing station operations in the coffee shop.
 *
 * @author Christy Zheng
 * @version 1.0
 * @since 2026-04-17
 * @modifiedby Kulagina Tatiana
 * @since 2026-04-28
 */
@Service
public class StationService {
    private final StationRepository stationRepository;

    /**
     * Creates a new StationService instance.
     *
     * @param stationRepository the repository for station database operations
     */
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    /**
     * Retrieves all stations from the database.
     *
     * @return a list of all stations as DTOs
     */
    public List<StationDto> getAllStations() {
        return stationRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Retrieves a station by its ID.
     *
     * @param stationId the station ID to look up
     * @return the corresponding station as a DTO
     * @throws EntityNotFoundException if no station with the given ID exists
     */
    public StationDto getStationById(Long stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("Station with id " + stationId + " not found!"));
        return toDto(station);
    }

    // public boolean isOpen(Long stationId, LocalDateTime dateTime) {
    //     Station station = stationRepository.findById(stationId)
    //             .orElseThrow(() -> new EntityNotFoundException("Station with id " + stationId + " not found!"));

    //     DayOfWeek day = dateTime.getDayOfWeek();
    //     LocalTime time = dateTime.toLocalTime();

    //     if (day == DayOfWeek.SUNDAY && station.isClosedOnSunday()) return false;

    //     String hours = (day == DayOfWeek.SATURDAY)
    //             ? station.getSaturdayOpeningHours()
    //             : station.getWeekdayOpeningHours();

    //     if (hours == null || !hours.contains("-")) return false;

    //     String[] parts = hours.split("-");
    //     LocalTime open = LocalTime.parse(parts[0].trim());
    //     LocalTime close = LocalTime.parse(parts[1].trim());

    //     return !time.isBefore(open) && !time.isAfter(close);
    // }

    // public void validatePickupTime(Long stationId, LocalDateTime pickupTime) {
    //     if (!isOpen(stationId, pickupTime)) {
    //         throw new IllegalArgumentException("Invalid pickup time!");
    //     }
    // }

    /**
     * Updates the opening hours of an existing station identified by the given ID.
     * Validates that opening times are strictly before closing times
     * for both weekday and saturday hours.
     *
     * @param stationId the ID of the station to update
     * @param dto the updated station schedule data
     * @return the updated station as a DTO
     * @throws EntityNotFoundException if no station with the given ID exists
     * @throws IllegalArgumentException if opening time is not before closing time
     */
    public StationDto updateOpeningHours(Long stationId, UpdateStationDto dto) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("Station with id " + stationId + " not found!"));

        String[] weekdays = dto.weekdayOpeningHours().split("-");
        String[] weekends = dto.saturdayOpeningHours().split("-");
        LocalTime openWeekdays = LocalTime.parse(weekdays[0].trim());
        LocalTime closeWeekdays = LocalTime.parse(weekdays[1].trim());
        LocalTime openWeekends = LocalTime.parse(weekends[0].trim());
        LocalTime closeWeekends = LocalTime.parse(weekends[1].trim());

        if (!openWeekdays.isBefore(closeWeekdays)) {
            throw new IllegalArgumentException("Weekdays opening time must be before closing time");
        }
        if (!openWeekends.isBefore(closeWeekends)) {
            throw new IllegalArgumentException("Weekends opening time must be before closing time");
        }
        
        station.updateSchedule(dto.weekdayOpeningHours(), dto.saturdayOpeningHours(), dto.closedOnSunday());

        return toDto(stationRepository.save(station));
    }

    /**
     * Converts a Station entity to a StationDto.
     *
     * @param station the station entity to convert
     * @return the corresponding StationDto
     */
    private StationDto toDto(Station station) {
        return new StationDto(
                station.getId(),
                station.getName(),
                station.getWeekdayOpeningHours(),
                station.getSaturdayOpeningHours(),
                station.isClosedOnSunday()
        );
    }
}