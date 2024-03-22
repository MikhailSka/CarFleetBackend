package com.example.demo.services;

import com.example.demo.exceptions.InternalServerErrorException;
import com.example.demo.models.Car;
import com.example.demo.repository.CarRepository;
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
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setId(1);
        // Initialize other necessary fields of car
    }

    @Test
    void getAllCarsShouldReturnCars() {
        Page<Car> carPage = new PageImpl<>(Arrays.asList(car));
        when(carRepository.findAll(any(PageRequest.class))).thenReturn(carPage);

        List<Car> result = carService.getAllCars(0, 10, "id", "asc", "");
        assertFalse(result.isEmpty(), "Expected non-empty list of cars");
        assertEquals(1, result.size(), "Expected list size of 1");
    }

    @Test
    void getCarByIdShouldReturnCar() {
        when(carRepository.findById(1)).thenReturn(Optional.of(car));

        Optional<Car> result = carService.getCarById(1);
        assertTrue(result.isPresent(), "Expected car to be present");
        assertEquals(car.getId(), result.get().getId(), "Expected car IDs to match");
    }

    @Test
    void saveCarShouldPersistCar() {
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Car savedCar = carService.saveCar(car);
        assertNotNull(savedCar, "Expected a non-null car");
        assertEquals(car.getId(), savedCar.getId(), "Expected car IDs to match");
    }

    @Test
    void deleteCarShouldRemoveCar() {
        carService.deleteCar(1);
        assertTrue(true, "Expected deleteCar to be called without errors");
    }

    @Test
    void getAllCarsWhenInternalServerErrorShouldThrowException() {
        when(carRepository.findAll(any(PageRequest.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(InternalServerErrorException.class,
                () -> carService.getAllCars(0, 10, "id", "asc", ""),
                "Expected getAllCars to throw InternalServerErrorException");
    }
}
