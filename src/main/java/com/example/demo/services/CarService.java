package com.example.demo.services;

import com.example.demo.exceptions.InternalServerErrorException;
import com.example.demo.models.Car;
import com.example.demo.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars(int page, int size, String sortBy, String sortOrder, String keyword) {
        try {
            Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<Car> carPage;
            if (keyword != null && !keyword.isEmpty()) {
                carPage = carRepository.searchCarsByKeyword(keyword.toLowerCase(), pageable);
            } else {
                carPage = carRepository.findAll(pageable);
            }
            return carPage.getContent();
        } catch (Exception e) {
            // Log the exception
            throw new InternalServerErrorException("An error occurred while retrieving cars");
        }
    }

    public Optional<Car> getCarById(int id) {
        try {
            return carRepository.findById(id);
        } catch (Exception e) {
            // Log the exception
            throw new InternalServerErrorException("An error occurred while retrieving the car by id: " + id);
        }
    }

    public Car saveCar(Car car) {
        try {
            return carRepository.save(car);
        } catch (Exception e) {
            // Log the exception
            throw new InternalServerErrorException("An error occurred while saving the car");
        }
    }

    public void deleteCar(int id) {
        try {
            carRepository.deleteById(id);
        } catch (Exception e) {
            // Log the exception
            throw new InternalServerErrorException("An error occurred while deleting the car with id: " + id);
        }
    }

    public void rentCar(int carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            // Sprawdź, czy samochód jest dostępny do wypożyczenia
            if (car.isAvailable()) {
                // Ustaw dostępność samochodu na false, aby oznaczyć go jako wypożyczony
                car.setAvailable(false);
                // Zapisz zmiany w bazie danych
                carRepository.save(car);
            } else {
                throw new IllegalStateException("Car is not available for rental.");
            }
        } else {
            throw new IllegalArgumentException("Car with id " + carId + " does not exist.");
        }
    }

}
