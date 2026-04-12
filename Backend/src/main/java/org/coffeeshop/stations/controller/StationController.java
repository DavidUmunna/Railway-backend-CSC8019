package org.coffeeshop.stations.controller;

import org.coffeeshop.stations.dtos.StationDto;
import org.coffeeshop.stations.service.StationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Currently support Cramlington Station, but structure allows future expansion
 */
@RestController
@RequestMapping("/api/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    // Return all stations (used if frontend needs to add new locations)
    @GetMapping
    public List<StationDto> getAllStations() {
        return stationService.getAllStations();
    }

    // Return a single station by id
    @GetMapping("/{id}")
    public StationDto getStation(@PathVariable int id) {
        return stationService.getStationById(id);
    }

    // TBC:
    // Could expose an endpoint to check if a station is open for a given pickup time
}