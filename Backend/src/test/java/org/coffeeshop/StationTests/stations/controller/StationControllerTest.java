package org.coffeeshop.StationTests.stations.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.coffeeshop.security.JwtAuthenticationFilter;
import org.coffeeshop.security.JwtService;
import org.coffeeshop.stations.controller.StationController;
import org.coffeeshop.stations.dtos.StationDto;
import org.coffeeshop.stations.services.StationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StationController.class)
@AutoConfigureMockMvc(addFilters = false)
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    JwtService jwtService;
    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private StationService stationService;

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