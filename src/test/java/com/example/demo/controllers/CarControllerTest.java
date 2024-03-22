package com.example.demo.controllers;

import com.example.demo.config.TestSecurityConfig;
import com.example.demo.models.Car;
import com.example.demo.services.CarService;
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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CarController.class)
@Import(TestSecurityConfig.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setId(1);
        // Initialize other necessary fields of car
    }

    @Test
    void getAllCarsShouldReturnCars() throws Exception {
        given(carService.getAllCars(0, 10, "id", "asc", null)).willReturn(Arrays.asList(car));
        mockMvc.perform(get("/api/cars")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortOrder", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(car.getId()));
    }

    @Test
    void getCarByIdShouldReturnCar() throws Exception {
        given(carService.getCarById(1)).willReturn(Optional.of(car));

        mockMvc.perform(get("/api/cars/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(car.getId()));
    }

    @Test
    void getCarByIdWhenNotFoundShouldReturnNotFound() throws Exception {
        given(carService.getCarById(1)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/cars/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCarShouldReturnCreated() throws Exception {
        given(carService.saveCar(any(Car.class))).willReturn(car);

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(car.getId()));
    }

    @Test
    void deleteCarShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/cars/{id}", car.getId()))
                .andExpect(status().isNoContent());
    }
}