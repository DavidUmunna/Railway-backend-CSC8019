package org.coffeeshop.stations.controller;

import org.coffeeshop.stations.dtos.StationDto;
import org.coffeeshop.stations.service.StationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stations") // 加上 v1 版本控制
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public List<StationDto> getAllStations() {
        return stationService.getAllStations();
    }

    @GetMapping("/{id}")
    public StationDto getStation(@PathVariable int id) {
        return stationService.getStationById(id);
    }
}