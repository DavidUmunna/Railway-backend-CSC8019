package org.coffeeshop.stations.controller;

import org.coffeeshop.stations.dtos.StationDto;
import org.coffeeshop.stations.service.StationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    // Get all stations
    @GetMapping
    public List<StationDto> getAllStations() {
        return stationService.getAllStations();
    }

    // Get one station by id
    @GetMapping("/{id}")
    public StationDto getStation(@PathVariable int id) {
        return stationService.getStationById(id);
    }
}