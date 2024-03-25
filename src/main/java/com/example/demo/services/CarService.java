package com.example.demo.services;

import com.example.demo.exceptions.InternalServerErrorException;
import com.example.demo.models.Car;
import com.example.demo.repository.CarRepository;
import com.example.demo.responses.GenericResponse;
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

    public GenericResponse getAllCars(int page, int size, String sortBy, String sortOrder, String keyword) {
        try {
            Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<Car> carPage;
            if (keyword != null && !keyword.isEmpty()) {
                carPage = carRepository.searchCarsByKeyword(keyword.toUpperCase(), pageable);
            } else {
                carPage = carRepository.findAll(pageable);
            }

            List<Car> cars = carPage.getContent();
            long totalCars = carPage.getTotalElements(); // This is how you get the total count

            return new GenericResponse(cars, totalCars); // Assuming you have a CarResponse class as described earlier
        } catch (Exception e) {
            // Log the exception and/or handle it appropriately
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

}
