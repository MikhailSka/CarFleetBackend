package com.example.demo.controllers;

import com.example.demo.config.TestSecurityConfig;
import com.example.demo.models.Trip;
import com.example.demo.responses.GenericResponse;
import com.example.demo.services.TripService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TripController.class)
@Import(TestSecurityConfig.class)
public class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TripService tripService;

    @Autowired
    private ObjectMapper objectMapper;

    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = new Trip();
        trip.setId(1);
    }

    @Test
    void getAllTripsShouldReturnTrips() throws Exception {
        Trip trip = new Trip(); // Setup your Trip object with mock data
        // Initialize trip properties as needed

        GenericResponse tripResponse = new GenericResponse(Collections.singletonList(trip), 1);
        given(tripService.getAllTrips(0, 10, "id", "asc", null)).willReturn(tripResponse);

        mockMvc.perform(get("/api/trips")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id").value(trip.getId()))
                .andExpect(jsonPath("$.total").value(1));
    }


    @Test
    void getTripByIdShouldReturnTrip() throws Exception {
        given(tripService.getTripById(1)).willReturn(Optional.of(trip));

        mockMvc.perform(get("/api/trips/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(trip.getId()));
    }

    @Test
    void getTripByIdWhenNotFoundShouldReturnNotFound() throws Exception {
        given(tripService.getTripById(1)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/trips/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTripShouldReturnCreated() throws Exception {
        given(tripService.saveTrip(any(Trip.class))).willReturn(trip);

        mockMvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trip)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(trip.getId()));
    }

    @Test
    void deleteTripShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/trips/{id}", trip.getId()))
                .andExpect(status().isNoContent());
    }
}
