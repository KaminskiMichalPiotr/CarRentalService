package com.example.carrentservice.services;

import com.example.carrentservice.entities.Car;
import com.example.carrentservice.entities.CarReservation;
import com.example.carrentservice.entities.CarReservationStatus;
import com.example.carrentservice.exceptions.IncorrectIdentifierException;
import com.example.carrentservice.exceptions.IncorrectReservationDateException;
import com.example.carrentservice.repositories.CarRepository;
import com.example.carrentservice.repositories.CarReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CarReservationService {

    private final CarReservationRepository carReservationRepository;
    private final CarRepository carRepository;
    private final Clock clock;

    public CarReservation createCarReservation(CarReservation carReservation) {
        if (carReservation.getEndOfRentalTime().isBefore(carReservation.getStartOfRentalTime()))
            throw new IncorrectReservationDateException("ERROR: Incorrect start and end day");
        boolean isAvailable = verifyIfCarIsAvailableBetweenDates(
                carReservation.getCar(),
                carReservation.getStartOfRentalTime(),
                carReservation.getEndOfRentalTime());
        if (!isAvailable) {
            throw new IncorrectReservationDateException("ERROR: Car is already taken between start and end day");
        } else {
            carReservation.setId(null);
            carReservation.setCarReservationStatus(CarReservationStatus.PENDING);
            carReservation.setCreatedAt(LocalDateTime.now(clock));
            return carReservationRepository.save(carReservation);
        }
    }

    private boolean verifyIfCarIsAvailableBetweenDates(Car car, LocalDate startOfRentalTime, LocalDate endOfRentalTime) {
        return findAvailableCarsByDate(startOfRentalTime, endOfRentalTime)
                .stream()
                .map(Car::getId)
                .toList()
                .contains(car.getId());
    }


    public List<Car> findAvailableCarsByDate(LocalDate start, LocalDate end) {
        if (start.isBefore(end))
            throw new IncorrectReservationDateException("ERROR: Incorrect start and end day");
        List<Car> cars = carRepository.findAll();
        List<Car> bookedCarsByStartDate = carReservationRepository
                .findCarReservationByStartOfRentalTimeIsBetween(start, end)
                .stream()
                .map(CarReservation::getCar)
                .toList();
        List<Car> bookedCarsByEndDate = carReservationRepository
                .findCarReservationByEndOfRentalTimeIsBetween(start, end)
                .stream()
                .map(CarReservation::getCar)
                .toList();
        cars.removeAll(bookedCarsByStartDate);
        cars.removeAll(bookedCarsByEndDate);
        return cars;
    }

    public CarReservation updateReservation(CarReservation carReservation) {
        if (carReservation.getId() == null) {
            throw new IncorrectIdentifierException(
                    "ERROR: Car ID is NULL");
        } else {
            carReservationRepository.findById(carReservation.getId())
                    .orElseThrow(() -> new IncorrectIdentifierException(
                            String.format("ERROR: Car with ID=%d doesn't exists", carReservation.getId())));
            return carReservationRepository.save(carReservation);
        }
    }
}
