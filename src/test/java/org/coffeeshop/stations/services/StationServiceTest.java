package org.coffeeshop.stations.services;

import org.coffeeshop.stations.dtos.StationDto;
import org.coffeeshop.stations.models.Station;
import org.coffeeshop.stations.repositories.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
        Station mockStation = new Station("Cramlington", "08:00-18:00", "09:00-17:00", false);

        when(stationRepository.findAll()).thenReturn(List.of(mockStation));

        // Act
        List<StationDto> result = stationService.getAllStations();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Cramlington", result.get(0).name());
    }

   
    @Test
    void shouldThrowExceptionWhenIdNotFound() {
        // Arrange
        when(stationRepository.findById(999L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            stationService.getStationById(999L);
        });
    }
}