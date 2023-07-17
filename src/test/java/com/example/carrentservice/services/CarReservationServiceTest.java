package com.example.carrentservice.services;

import com.example.carrentservice.entities.Car;
import com.example.carrentservice.entities.CarReservation;
import com.example.carrentservice.entities.CarReservationStatus;
import com.example.carrentservice.exceptions.IncorrectIdentifierException;
import com.example.carrentservice.exceptions.IncorrectReservationDateException;
import com.example.carrentservice.repositories.CarRepository;
import com.example.carrentservice.repositories.CarReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarReservationServiceTest {

    private CarReservationService carReservationService;

    @Mock
    private CarReservationRepository carReservationRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private Clock clock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        carReservationService = new CarReservationService(carReservationRepository, carRepository, clock);
    }

    @Test
    public void testCreateCarReservation_WithValidData_ReturnsCarReservation() {
        // Prepare test data
        Car car = new Car();
        car.setId(1L);
        car.setManufacturer("Manufacturer");
        carRepository.save(car);

        CarReservation carReservation = new CarReservation();
        carReservation.setCar(car);
        carReservation.setStartOfRentalTime(LocalDate.now());
        carReservation.setEndOfRentalTime(LocalDate.now().plusDays(1));

        // Mock behavior
        Mockito.when(clock.instant()).thenReturn(Instant.parse("2023-07-20T10:00:00Z"));
        Mockito.when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
        Mockito.when(carReservationRepository.save(carReservation)).thenReturn(carReservation);

        // Perform the test
        CarReservation createdReservation = carReservationService.createCarReservation(carReservation);

        // Assert the result
        assertNotNull(createdReservation.getId());
        assertEquals(CarReservationStatus.PENDING, createdReservation.getCarReservationStatus());
        assertEquals(LocalDateTime.ofInstant(Instant.parse("2023-07-20T10:00:00Z"), ZoneId.of("UTC")), createdReservation.getCreatedAt());
        Mockito.verify(carReservationRepository, Mockito.times(1)).save(carReservation);
    }

    @Test
    public void testCreateCarReservation_WithInvalidData_ThrowsException() {
        // Prepare test data
        CarReservation carReservation = new CarReservation();
        carReservation.setEndOfRentalTime(LocalDate.now());
        carReservation.setStartOfRentalTime(LocalDate.now().plusDays(1));

        // Perform the test and assert the exception
        assertThrows(IncorrectReservationDateException.class,
                () -> carReservationService.createCarReservation(carReservation));
        Mockito.verify(carReservationRepository, Mockito.never()).save(Mockito.any(CarReservation.class));
    }

    @Test
    public void testFindAvailableCarsByDate_ReturnsAvailableCars() {
        // Prepare test data
        Car car1 = new Car();
        car1.setId(1L);
        car1.setManufacturer("Manufacturer1");

        Car car2 = new Car();
        car2.setId(2L);
        car2.setManufacturer("Manufacturer2");

        CarReservation reservation1 = new CarReservation();
        reservation1.setCar(car1);
        reservation1.setStartOfRentalTime(LocalDate.now().minusDays(2));
        reservation1.setEndOfRentalTime(LocalDate.now().minusDays(1));

        CarReservation reservation2 = new CarReservation();
        reservation2.setCar(car2);
        reservation2.setStartOfRentalTime(LocalDate.now().plusDays(1));
        reservation2.setEndOfRentalTime(LocalDate.now().plusDays(2));

        List<Car> allCars = new ArrayList<>();
        allCars.add(car1);
        allCars.add(car2);

        // Mock behavior
        Mockito.when(carRepository.findAll()).thenReturn(allCars);
        Mockito.when(carReservationRepository.findCarReservationByStartOfRentalTimeIsBetween(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
                .thenReturn(List.of(reservation1));
        Mockito.when(carReservationRepository.findCarReservationByEndOfRentalTimeIsBetween(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
                .thenReturn(List.of(reservation2));

        // Perform the test
        List<Car> availableCars = carReservationService.findAvailableCarsByDate(LocalDate.now(), LocalDate.now().plusDays(3));

        // Assert the result
        assertEquals(1, availableCars.size());
        assertEquals(car1, availableCars.get(0));
    }

    @Test
    public void testUpdateReservation_WithExistingId_ReturnsUpdatedReservation() {
        // Prepare test data
        CarReservation carReservation = new CarReservation();
        carReservation.setId(1L);
        carReservation.setStartOfRentalTime(LocalDate.now().plusDays(1));
        carReservation.setEndOfRentalTime(LocalDate.now().plusDays(2));

        // Mock behavior
        Mockito.when(carReservationRepository.findById(carReservation.getId())).thenReturn(java.util.Optional.of(carReservation));
        Mockito.when(carReservationRepository.save(carReservation)).thenReturn(carReservation);

        // Perform the test
        CarReservation updatedReservation = carReservationService.updateReservation(carReservation);

        // Assert the result
        assertEquals(carReservation, updatedReservation);
        Mockito.verify(carReservationRepository, Mockito.times(1)).findById(carReservation.getId());
        Mockito.verify(carReservationRepository, Mockito.times(1)).save(carReservation);
    }

    @Test
    public void testUpdateReservation_WithNullId_ThrowsException() {
        // Prepare test data
        CarReservation carReservation = new CarReservation();
        carReservation.setStartOfRentalTime(LocalDate.now().plusDays(1));
        carReservation.setEndOfRentalTime(LocalDate.now().plusDays(2));

        // Perform the test and assert the exception
        assertThrows(IncorrectIdentifierException.class,
                () -> carReservationService.updateReservation(carReservation));
        Mockito.verify(carReservationRepository, Mockito.never()).findById(Mockito.anyLong());
        Mockito.verify(carReservationRepository, Mockito.never()).save(Mockito.any(CarReservation.class));
    }

    @Test
    public void testUpdateReservation_WithNonExistingId_ThrowsException() {
        // Prepare test data
        CarReservation carReservation = new CarReservation();
        carReservation.setId(1L);
        carReservation.setStartOfRentalTime(LocalDate.now().plusDays(1));
        carReservation.setEndOfRentalTime(LocalDate.now().plusDays(2));

        // Mock behavior
        Mockito.when(carReservationRepository.findById(carReservation.getId())).thenReturn(java.util.Optional.empty());

        // Perform the test and assert the exception
        assertThrows(IncorrectIdentifierException.class,
                () -> carReservationService.updateReservation(carReservation));
        Mockito.verify(carReservationRepository, Mockito.times(1)).findById(carReservation.getId());
        Mockito.verify(carReservationRepository, Mockito.never()).save(Mockito.any(CarReservation.class));
    }
}