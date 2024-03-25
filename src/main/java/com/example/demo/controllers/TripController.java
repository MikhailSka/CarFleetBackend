package com.example.demo.controllers;

import com.example.demo.exceptions.InternalServerErrorException;
import com.example.demo.models.Trip;
import com.example.demo.responses.GenericResponse;
import com.example.demo.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/trips")
public class TripController {
    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public ResponseEntity<GenericResponse> getAllTrips(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String keyword) {
        try {
            GenericResponse response = tripService.getAllTrips(page, size, sortBy, sortOrder, keyword);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable int id) {
        try {
            Optional<Trip> trip = tripService.getTripById(id);
            return trip.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody Trip trip) {
        try {
            Trip newTrip = tripService.saveTrip(trip);
            return new ResponseEntity<>(newTrip, HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable int id) {
        try {
            tripService.deleteTrip(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
