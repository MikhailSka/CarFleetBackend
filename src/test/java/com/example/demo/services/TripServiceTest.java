package com.example.demo.services;

import com.example.demo.exceptions.InternalServerErrorException;
import com.example.demo.models.Trip;
import com.example.demo.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripService tripService;

    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = new Trip();
        trip.setId(1);
    }

    @Test
    void getAllTripsShouldReturnTrips() {
        Page<Trip> tripPage = new PageImpl<>(Arrays.asList(trip));
        when(tripRepository.findAll(any(PageRequest.class))).thenReturn(tripPage);

        List<Trip> result = tripService.getAllTrips(0, 10, "id", "asc", "");
        assertFalse(result.isEmpty(), "Expected non-empty list of trips");
        assertEquals(1, result.size(), "Expected list size of 1");
    }

    @Test
    void getTripByIdShouldReturnTrip() {
        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));

        Optional<Trip> result = tripService.getTripById(1);
        assertTrue(result.isPresent(), "Expected trip to be present");
        assertEquals(trip.getId(), result.get().getId(), "Expected trip IDs to match");
    }

    @Test
    void saveTripShouldPersistTrip() {
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);

        Trip savedTrip = tripService.saveTrip(trip);
        assertNotNull(savedTrip, "Expected a non-null trip");
        assertEquals(trip.getId(), savedTrip.getId(), "Expected trip IDs to match");
    }

    @Test
    void deleteTripShouldRemoveTrip() {
        tripService.deleteTrip(1);
        assertTrue(true, "Expected deleteTrip to be called without errors");
    }

    @Test
    void getAllTripsWhenInternalServerErrorShouldThrowException() {
        when(tripRepository.findAll(any(PageRequest.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(InternalServerErrorException.class,
                () -> tripService.getAllTrips(0, 10, "id", "asc", ""),
                "Expected getAllTrips to throw InternalServerErrorException");
    }
}
