package com.example.demo.services;

import com.example.demo.exceptions.InternalServerErrorException;
import com.example.demo.models.Trip;
import com.example.demo.repository.TripRepository;
import com.example.demo.responses.GenericResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
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
        // Prepare a mock Trip
        Trip trip = new Trip();
        trip.setId(1); // Set other necessary properties as needed

        // Mocking the Pageable response from the repository
        Page<Trip> tripPage = new PageImpl<>(Collections.singletonList(trip));
        when(tripRepository.findAll(any(Pageable.class))).thenReturn(tripPage);

        // Call the service method
        GenericResponse result = tripService.getAllTrips(0, 10, "id", "asc", "");

        // Assertions to verify the response
        assertFalse(result.getData().isEmpty(), "Expected non-empty list of trips");
        assertEquals(1, result.getData().size(), "Expected list size of 1");
        assertEquals(1, result.getTotal(), "Expected total count of 1");
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
