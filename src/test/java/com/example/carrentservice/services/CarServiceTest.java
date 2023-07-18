package com.example.carrentservice.services;

import com.example.carrentservice.entities.Car;
import com.example.carrentservice.exceptions.IncorrectIdentifierException;
import com.example.carrentservice.repositories.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveNewCar_NewCar_SuccessfullySavesCar() {
        // Prepare test data
        Car car = new Car();
        car.setId(null);

        // Mock behavior
        when(carRepository.save(any(Car.class))).thenReturn(car);

        // Perform the test
        Car result = carService.saveNewCar(car);

        // Assert the result
        assertNotNull(result);
    }

    @Test
    void updateCar_ExistingCar_SuccessfullyUpdatesCar() {
        // Prepare test data
        Long carId = 1L;
        Car car = new Car();
        car.setId(carId);

        // Mock behavior
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenReturn(car);

        // Perform the test
        Car result = carService.updateCar(car);

        // Assert the result
        assertNotNull(result);
        assertEquals(carId, result.getId());
    }

    @Test
    void updateCar_NullCarId_ThrowsIncorrectIdentifierException() {
        // Prepare test data
        Car car = new Car();
        car.setId(null);

        // Perform the test and assert the exception
        assertThrows(IncorrectIdentifierException.class, () -> carService.updateCar(car));
    }

    @Test
    void updateCar_NonExistingCarId_ThrowsIncorrectIdentifierException() {
        // Prepare test data
        Long carId = 1L;
        Car car = new Car();
        car.setId(carId);

        // Mock behavior
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Perform the test and assert the exception
        assertThrows(IncorrectIdentifierException.class, () -> carService.updateCar(car));
    }

    @Test
    void findCarById_ExistingCarId_ReturnsCar() {
        // Prepare test data
        Long carId = 1L;
        Car car = new Car();
        car.setId(carId);

        // Mock behavior
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // Perform the test
        Car result = carService.findCarById(carId);

        // Assert the result
        assertNotNull(result);
        assertEquals(carId, result.getId());
    }

    @Test
    void findCarById_NonExistingCarId_ThrowsIncorrectIdentifierException() {
        // Prepare test data
        Long carId = 1L;

        // Mock behavior
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Perform the test and assert the exception
        assertThrows(IncorrectIdentifierException.class, () -> carService.findCarById(carId));
    }

}