package org.coffeeshop.stations.controller;

import org.coffeeshop.stations.dtos.StationDtos;
import org.coffeeshop.stations.models.Station;
import org.coffeeshop.stations.repositories.StationRepository;
import org.coffeeshop.stations.service.StationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stations")
public class StationController {

    private final StationRepository repository;
    private final StationService service;

    public StationController(StationRepository repository, StationService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    public List<StationDtos> getAllStations() {
        return repository.findAll().stream()
                .map(service::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public StationDtos getStation(@PathVariable int id) {
        Station station = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found"));
        return service.convertToDto(station);
    }
}