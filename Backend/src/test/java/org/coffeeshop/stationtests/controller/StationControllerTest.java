package org.coffeeshop.stationtests.controller;

import org.coffeeshop.stations.controller.StationController;
import org.coffeeshop.stations.dtos.StationDto;
import org.coffeeshop.stations.services.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Unit tests for StationController using MockMvc to simulate HTTP requests and responses.
 * @author Christy Zheng
 * @version 1.0
 * @since 14/04/2026
 * @ModifiedBy Umunna David
 * @since 17/04/2026
 */
@ExtendWith(MockitoExtension.class)
class StationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StationService stationService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new StationController(stationService)).build();
    }

    @Test
    void shouldReturnAllStationsFromApi() throws Exception {
        // Arrange
        StationDto mockDto = new StationDto(1L, "Cramlington", "08:00-18:00", "09:00-17:00", true);
        when(stationService.getAllStations()).thenReturn(List.of(mockDto));

        // Act & Assert
        mockMvc.perform(get("/api/v1/stations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Cramlington"));
    }

    @Test
    void shouldReturnStationByIdFromApi() throws Exception {
        // Arrange
        StationDto mockDto = new StationDto(1L, "Cramlington", "08:00-18:00", "09:00-17:00", true);
        when(stationService.getStationById(1L)).thenReturn(mockDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/stations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Cramlington"));
    }
}