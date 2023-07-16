package com.example.carrentservice.reservation;

import com.example.carrentservice.exceptions.IncorrectReservationDateException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CarReservationService {

    private final CarReservationRepository carReservationRepository;

    public CarReservation createCarReservation(CarReservation carReservation) {
        if(carReservation.getEndOfRentalTime().isBefore(carReservation.getStartOfRentalTime()))
            throw new IncorrectReservationDateException("ERROR: Incorrect start and end day");

        carReservation.setId(null);
        carReservation.setCarReservationStatus(CarReservationStatus.PENDING);
        carReservation.setCreatedAt(LocalDateTime.now());
        return carReservationRepository.save(carReservation);
    }


}
