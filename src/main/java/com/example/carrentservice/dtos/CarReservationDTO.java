package com.example.carrentservice.dtos;

import com.example.carrentservice.entities.CarReservation;
import com.example.carrentservice.entities.CarReservationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CarReservationDTO {



    @NotNull(message = "ERROR: Car id cannot be null")
    private final Long carId;
    @NotNull(message = "ERROR: Client id cannot be null")
    private final Long clientId;
    @NotNull(message = "ERROR: Invalid start of rental time: null")
    private final LocalDate startOfRentalTime;
    @NotNull(message = "ERROR: Invalid end of rental time: null")
    private final LocalDate endOfRentalTime;
    private final CarReservationStatus carReservationStatus;
    private final LocalDateTime createdAt;


    public CarReservationDTO(CarReservation entity) {
        this.carId = entity.getCar().getId();
        this.clientId = entity.getClient().getId();
        this.startOfRentalTime = entity.getStartOfRentalTime();
        this.endOfRentalTime = entity.getEndOfRentalTime();
        this.carReservationStatus = entity.getCarReservationStatus();
        this.createdAt = entity.getCreatedAt();
    }


}
