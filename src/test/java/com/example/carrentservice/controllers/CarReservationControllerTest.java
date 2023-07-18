package com.example.carrentservice.controllers;

import com.example.carrentservice.dtos.CarReservationConfigurationDTO;
import com.example.carrentservice.entities.*;
import com.example.carrentservice.repositories.CarRepository;
import com.example.carrentservice.repositories.CarReservationRepository;
import com.example.carrentservice.repositories.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CarReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CarReservationRepository carReservationRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void getCarReservations_ReturnsList() throws Exception {
        Car car = carRepository.save(mockCar());
        Client client = clientRepository.save(mockClient());
        carReservationRepository.save(mockCarReservation(car,client));

        mockMvc.perform(get("/api/v1/reservation"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void createReservation_ValidReservation_ReturnsOk() throws Exception {
        Car car = carRepository.save(mockCar());
        Client client = clientRepository.save(mockClient());
        CarReservation carReservation = mockCarReservation(car,client);


        ResultActions resultActions = mockMvc.perform(post("/api/v1/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carReservation)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(Matchers.notNullValue()));
    }

    @Test
    void createReservation_InvalidReservation_ReturnsBadRequest() throws Exception {
        CarReservation carReservation = new CarReservation(); // Invalid reservation with missing required fields
        carReservation.setCar(carRepository.save(mockCar()));
        carReservation.setStartOfRentalTime(LocalDate.now());

        mockMvc.perform(post("/api/v1/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carReservation)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createReservation_InvalidDates_ReturnsUnprocessableEntity() throws Exception {
        Car car = carRepository.save(mockCar());
        Client client = clientRepository.save(mockClient());
        CarReservation carReservation = mockCarReservation(car,client);
        carReservation.setStartOfRentalTime(LocalDate.now());
        carReservation.setEndOfRentalTime(LocalDate.now().minusDays(1)); // Invalid dates

        mockMvc.perform(post("/api/v1/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carReservation)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Reservation dates Error"));
    }

    @Test
    void updateReservation_ValidReservation_ReturnsOk() throws Exception {
        Car car = carRepository.save(mockCar());
        Client client = clientRepository.save(mockClient());
        CarReservation carReservation = carReservationRepository.save(mockCarReservation(car,client));
        carReservation.setEndOfRentalTime(carReservation.getEndOfRentalTime().plusDays(1));


        ResultActions resultActions = mockMvc.perform(put("/api/v1/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carReservation)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.endOfRentalTime").value(carReservation.getEndOfRentalTime().toString()));

    }

    @Test
    void updateReservation_InvalidReservation_ReturnsBadRequest() throws Exception {
        CarReservation carReservation = new CarReservation(); // Invalid reservation with missing required fields

        mockMvc.perform(put("/api/v1/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carReservation)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateReservation_NonExistingId_ReturnsNotFound() throws Exception {
        Car car = carRepository.save(mockCar());
        Client client = clientRepository.save(mockClient());
        CarReservation carReservation = carReservationRepository.save(mockCarReservation(car,client));
        carReservation.setId(Long.MAX_VALUE); // Non-existing ID


        mockMvc.perform(put("/api/v1/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carReservation)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Identifier Error"));
    }

    @Test
    void findAvailableCars_ValidConfiguration_ReturnsOk() throws Exception {
        Car car = carRepository.save(mockCar());

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);
        CarReservationConfigurationDTO configurationDTO = new CarReservationConfigurationDTO(startDate, endDate);

        mockMvc.perform(get("/api/v1/reservation/availableCars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configurationDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].manufacturer").value(car.getManufacturer()))
                .andExpect(jsonPath("$[0].numberOfSeats").value(car.getNumberOfSeats()))
                .andExpect(jsonPath("$[0].carType").value(car.getCarType().toString()))
                .andExpect(jsonPath("$[0].yearOfProduction").value(car.getYearOfProduction()));
    }

    @Test
    void findAvailableCars_InvalidConfiguration_ReturnsBadRequest() throws Exception {
        Car car = carRepository.save(mockCar());

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().minusDays(1); // Invalid dates
        CarReservationConfigurationDTO configurationDTO = new CarReservationConfigurationDTO(startDate, endDate);

        mockMvc.perform(get("/api/v1/reservation/availableCars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(configurationDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Reservation dates Error"));

    }

    private Car mockCar(){
        return new Car("Manufacturer", 4, CarType.SEDAN, 2022);
    }

    private Client mockClient(){
        return new Client("Joe", "Doe", "joe.doe@gmail.com", "+01234567891");
    }

    private CarReservation mockCarReservation(Car car, Client client){
        return new CarReservation(
                car,
                client,
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                CarReservationStatus.PENDING,
                null);
    }

}