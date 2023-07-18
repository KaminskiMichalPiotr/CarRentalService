package com.example.carrentservice.controllers;

import com.example.carrentservice.entities.Car;
import com.example.carrentservice.entities.CarType;
import com.example.carrentservice.exceptions.IncorrectIdentifierException;
import com.example.carrentservice.services.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CarControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarService carService;

    @Test
    void saveNewCar_ValidCar_ReturnsOk() throws Exception {
        Car car = new Car("Manufacturer", 4, CarType.SEDAN, 2022);
        when(carService.saveNewCar(any(Car.class))).thenReturn(car);

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
        when(carService.updateCar(any(Car.class))).thenReturn(car);

        ResultActions resultActions = mockMvc.perform(put("/api/v1/car")
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
    void updateCar_InvalidCar_ReturnsBadRequest() throws Exception {
        Car car = new Car("Manufacturer", 1, CarType.SEDAN, 2022);

        mockMvc.perform(put("/api/v1/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getCarById_ExistingId_ReturnsOk() throws Exception {
        Long carId = 1L;
        Car car = new Car("Manufacturer", 4, CarType.SEDAN, 2022);
        when(carService.findCarById(carId)).thenReturn(car);

        mockMvc.perform(get("/api/v1/car/{id}", carId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.manufacturer").value(car.getManufacturer()))
                .andExpect(jsonPath("$.numberOfSeats").value(car.getNumberOfSeats()))
                .andExpect(jsonPath("$.carType").value(car.getCarType().name()))
                .andExpect(jsonPath("$.yearOfProduction").value(car.getYearOfProduction()));
    }

    @Test
    void getCarById_NonExistingId_ReturnsNotFound() throws Exception {
        Long carId = 1L;
        when(carService.findCarById(carId)).thenThrow(new IncorrectIdentifierException("err"));

        mockMvc.perform(get("/api/v1/car/{id}", carId))
                .andExpect(status().isNotFound());

    }
}