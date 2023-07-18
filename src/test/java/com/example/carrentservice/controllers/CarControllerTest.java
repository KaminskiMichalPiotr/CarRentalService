package com.example.carrentservice.controllers;

import com.example.carrentservice.entities.Car;
import com.example.carrentservice.entities.CarType;
import com.example.carrentservice.exceptions.IncorrectIdentifierException;
import com.example.carrentservice.repositories.CarRepository;
import com.example.carrentservice.services.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CarRepository carRepository;

    @Test
    void saveNewCar_ValidCar_ReturnsOk() throws Exception {
        Car car = new Car("Manufacturer", 4, CarType.SEDAN, 2022);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.manufacturer").value(car.getManufacturer()))
                .andExpect(jsonPath("$.numberOfSeats").value(car.getNumberOfSeats()))
                .andExpect(jsonPath("$.carType").value(car.getCarType().name()))
                .andExpect(jsonPath("$.yearOfProduction").value(car.getYearOfProduction()));

    }

    @Test
    void saveNewCar_InvalidCar_ReturnsBadRequest() throws Exception {
        Car car = new Car("Manufacturer", 1, CarType.SEDAN, 2022);

        mockMvc.perform(post("/api/v1/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCar_ValidCar_ReturnsOk() throws Exception {
        Car car = new Car("Manufacturer", 4, CarType.SEDAN, 2022);
        carRepository.save(car);
        car.setCarType(CarType.HATCHBACK);

        ResultActions resultActions = mockMvc.perform(put("/api/v1/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(car.getId()))
                .andExpect(jsonPath("$.manufacturer").value(car.getManufacturer()))
                .andExpect(jsonPath("$.numberOfSeats").value(car.getNumberOfSeats()))
                .andExpect(jsonPath("$.carType").value(car.getCarType().name()))
                .andExpect(jsonPath("$.yearOfProduction").value(car.getYearOfProduction()));

    }

    @Test
    void updateCar_InvalidCar_ReturnsBadRequest() throws Exception {
        Car car = new Car("Manufacturer", 1, CarType.SEDAN, 2022);

        mockMvc.perform(put("/api/v1/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getCarById_ExistingId_ReturnsOk() throws Exception {
        Car car = new Car("Manufacturer", 4, CarType.SEDAN, 2022);
        carRepository.save(car);
        Long carId = car.getId();

        mockMvc.perform(get("/api/v1/car/{id}", carId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(car.getId()))
                .andExpect(jsonPath("$.manufacturer").value(car.getManufacturer()))
                .andExpect(jsonPath("$.numberOfSeats").value(car.getNumberOfSeats()))
                .andExpect(jsonPath("$.carType").value(car.getCarType().name()))
                .andExpect(jsonPath("$.yearOfProduction").value(car.getYearOfProduction()));
    }

    @Test
    void getCarById_NonExistingId_ReturnsNotFound() throws Exception {
        Long carId = Long.MAX_VALUE;

        mockMvc.perform(get("/api/v1/car/{id}", carId))
                .andExpect(status().isNotFound());
    }
}