package com.example.carrentservice.repositories;

import com.example.carrentservice.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class CarReservationRepositoryTest {

    public static final int DAYS = 7;
    @Autowired
    private CarReservationRepository underTest;

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CarRepository carRepository;


    @Test
    void itShouldFindCarReservationByStartOfRentalTimeIsBetween() {
        //given
        LocalDate dateStart = LocalDate.of(2020, 1, 1);
        LocalDate dateEnd = dateStart.plusDays(DAYS);
        CarReservation carReservation = createCarReservation(dateStart, dateEnd);

        //when
        List<CarReservation> carReservationByStartOfRentalTimeIsBetween =
                underTest.findCarReservationByStartOfRentalTimeIsBetween(
                        dateStart.minusDays(1),
                        dateEnd.minusDays(1));

        //then
        boolean match = carReservationByStartOfRentalTimeIsBetween
                .stream()
                .map(CarReservation::getId)
                .anyMatch(id -> id.equals(carReservation.getId()));
        assertTrue(match);

    }

    @Test
    void itShouldNotFindCarReservationByStartOfRentalTimeIsBetweenWithDateOutsideOfRange() {
        //given
        LocalDate dateStart = LocalDate.of(2020, 1, 1);
        LocalDate dateEnd = dateStart.plusDays(DAYS);
        CarReservation carReservation = createCarReservation(dateStart, dateEnd);

        //when
        List<CarReservation> carReservationByStartOfRentalTimeIsBetween =
                underTest.findCarReservationByStartOfRentalTimeIsBetween(
                        dateStart.plusDays(DAYS + 1),
                        dateEnd.plusDays(DAYS + 2));

        //then
        boolean match = carReservationByStartOfRentalTimeIsBetween
                .stream()
                .map(CarReservation::getId)
                .anyMatch(id -> id.equals(carReservation.getId()));
        assertFalse(match);

    }

    @Test
    void itShouldFindCarReservationByEndOfRentalTimeIsBetween() {
        //given
        LocalDate dateStart = LocalDate.of(2020, 1, 1);
        LocalDate dateEnd = dateStart.plusDays(DAYS);
        CarReservation carReservation = createCarReservation(dateStart, dateEnd);

        //when
        List<CarReservation> carReservationByEndOfRentalTimeIsBetween =
                underTest.findCarReservationByEndOfRentalTimeIsBetween(
                        dateStart.plusDays(1),
                        dateEnd.plusDays(1));

        //then
        boolean match = carReservationByEndOfRentalTimeIsBetween
                .stream()
                .map(CarReservation::getId)
                .anyMatch(id -> id.equals(carReservation.getId()));
        assertTrue(match);
    }

    @Test
    void itShouldNotFindCarReservationByEndOfRentalTimeIsBetweenWithDateOutsideOfRange() {
        //given
        LocalDate dateStart = LocalDate.of(2020, 1, 1);
        LocalDate dateEnd = dateStart.plusDays(DAYS);
        CarReservation carReservation = createCarReservation(dateStart, dateEnd);

        //when
        List<CarReservation> carReservationByEndOfRentalTimeIsBetween =
                underTest.findCarReservationByEndOfRentalTimeIsBetween(
                        dateStart.minusDays(DAYS + 2),
                        dateEnd.minusDays(DAYS + 1));

        //then
        boolean match = carReservationByEndOfRentalTimeIsBetween
                .stream()
                .map(CarReservation::getId)
                .anyMatch(id -> id.equals(carReservation.getId()));
        assertFalse(match);

    }


    private CarReservation createCarReservation(LocalDate dateStart, LocalDate dateEnd) {
        Car car = new Car("Skoda", 5, CarType.HATCHBACK, 2020);
        carRepository.save(car);
        Client client = new Client("John", "Doe", "john.doe@doe.com", "+00000000000");
        clientRepository.save(client);
        CarReservation carReservation = new CarReservation(
                car,
                client,
                dateStart,
                dateEnd,
                CarReservationStatus.PENDING,
                LocalDateTime.now());
        underTest.save(carReservation);
        return carReservation;
    }
}