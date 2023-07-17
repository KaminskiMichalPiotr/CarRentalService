package com.example.carrentservice.dtos;

import com.example.carrentservice.entities.CarReservation;
import com.example.carrentservice.entities.CarReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CarReservationDTO {



    private final Long id;
    private final Long carId;
    private final Long clientId;
    private final LocalDate startOfRentalTime;
    private final LocalDate endOfRentalTime;
    private final CarReservationStatus carReservationStatus;
    private final LocalDateTime createdAt;


    public CarReservationDTO(CarReservation entity) {
        this.id = entity.getId();
        this.carId = entity.getCar().getId();
        this.clientId = entity.getClient().getId();
        this.startOfRentalTime = entity.getStartOfRentalTime();
        this.endOfRentalTime = entity.getEndOfRentalTime();
        this.carReservationStatus = entity.getCarReservationStatus();
        this.createdAt = entity.getCreatedAt();
    }


}
