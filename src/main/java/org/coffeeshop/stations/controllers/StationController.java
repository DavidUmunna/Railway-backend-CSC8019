package org.coffeeshop.stations.controllers;

import org.coffeeshop.stations.dtos.StationDto;
import org.coffeeshop.stations.dtos.UpdateStationDto;
import org.coffeeshop.stations.services.StationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

/**
 * REST controller for managing coffee shop stations.
 * Provides endpoints for retrieving stations and updating their opening hours.
 *
 * @author Christy Zheng
 * @version 1.0
 * @since 2026-04-17
 * @modifiedby Kulagina Tatiana
 * @since 2026-04-28
 */
@RestController
@RequestMapping("/api/v1/stations")
public class StationController {
    private final StationService stationService;

    /**
     * Creates a new StationController instance.
     *
     * @param stationService the service handling station business logic
     */
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    /**
     * GET /api/v1/stations — retrieves all stations.
     *
     * @return a list of all stations as DTOs
     */
    @GetMapping
    public List<StationDto> getAllStations() {
        return stationService.getAllStations();
    }

    /**
     * GET /api/v1/stations/{id} — retrieves a station by its ID.
     *
     * @param id the station ID
     * @return the corresponding station DTO
     */
    @GetMapping("/{id}")
    public StationDto getStation(@PathVariable("id") Long id) {
        return stationService.getStationById(id);
    }

    /**
     * PUT /api/v1/stations/{id}/update — updates a station's opening hours.
     *
     * @param id  the path ID identifying the station to update
     * @param dto the updated station schedule data, validated via {@link Valid}
     * @return the updated station DTO with 200 OK
     */
    @PutMapping("/{id}/update")
    public ResponseEntity<StationDto> updateStationHours(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateStationDto dto) {
        StationDto updated = stationService.updateOpeningHours(id, dto);

        return ResponseEntity.ok(updated);
    }
    
}
