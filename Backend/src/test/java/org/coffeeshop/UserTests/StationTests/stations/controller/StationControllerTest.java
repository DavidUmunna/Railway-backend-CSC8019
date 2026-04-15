package org.coffeeshop.UserTests.StationTests.stations.controller;

import org.coffeeshop.stations.controller.StationController;
import org.coffeeshop.stations.dtos.StationDto;
import org.coffeeshop.stations.service.StationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // 補上這個
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser; // 補上這個
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
    private StationService stationService;

    @Test
    @WithMockUser
    void shouldReturnAllStationsFromApi() throws Exception {
        // Arrange
        StationDto mockDto = new StationDto(1, "Cramlington", "08:00-18:00", "09:00-17:00", true);
        when(stationService.getAllStations()).thenReturn(List.of(mockDto));

        // Act & Assert
        mockMvc.perform(get("/api/v1/stations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Cramlington"));
    }

    @Test
    @WithMockUser
    void shouldReturnStationByIdFromApi() throws Exception {
        // Arrange
        StationDto mockDto = new StationDto(1, "Cramlington", "08:00-18:00", "09:00-17:00", true);
        when(stationService.getStationById(1)).thenReturn(mockDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/stations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Cramlington"));
    }
}