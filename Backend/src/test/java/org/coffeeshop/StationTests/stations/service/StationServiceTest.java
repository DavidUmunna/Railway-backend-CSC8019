package org.coffeeshop.StationTests.stations.service;

import org.coffeeshop.stations.dtos.StationDto;
import org.coffeeshop.stations.models.Station;
import org.coffeeshop.stations.repositories.StationRepository;
import org.coffeeshop.stations.service.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StationServiceTest {

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private StationService stationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllStations() {
        // Arrange
        Station mockStation = new Station();
        mockStation.setName("Cramlington");
        when(stationRepository.findAll()).thenReturn(List.of(mockStation));

        // Act
        List<StationDto> result = stationService.getAllStations();

        // Assert (Record uses .name() to access)
        assertEquals(1, result.size());
        assertEquals("Cramlington", result.get(0).name());
    }

    @Test
    void shouldReturnTrueWhenStationIsOpen() {
        // Arrange: simulate a station open Mon-Fri 08:00-18:00
        Station mockStation = new Station();
        mockStation.setWeekdayOpeningHours("08:00-18:00");
        when(stationRepository.findById(1)).thenReturn(Optional.of(mockStation));

        // Act: test Monday 2026-04-13 at 10:00 AM
        LocalDateTime mondayTenAm = LocalDateTime.of(2026, 4, 13, 10, 0);
        boolean isOpen = stationService.isOpen(1, mondayTenAm);

        // Assert
        assertTrue(isOpen, "Station should be open at this time");
    }

    @Test
    void shouldThrowExceptionWhenIdNotFound() {
        // Arrange
        when(stationRepository.findById(999)).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            stationService.getStationById(999);
        });
    }
}