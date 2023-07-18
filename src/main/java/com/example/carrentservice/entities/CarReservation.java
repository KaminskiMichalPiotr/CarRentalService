package com.example.carrentservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CarReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_ID")
    @NotNull(message = "ERROR: Car cannot be null")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @NotNull(message = "ERROR: Client cannot be null")
    private Client client;

    @NotNull(message = "ERROR: Invalid start of rental time: null")
    private LocalDate startOfRentalTime;

    @NotNull(message = "ERROR: Invalid end of rental time: null")
    private LocalDate endOfRentalTime;

    private CarReservationStatus carReservationStatus;

    private LocalDateTime createdAt;


    public CarReservation(Car car, Client client, LocalDate startOfRentalTime, LocalDate endOfRentalTime, CarReservationStatus carReservationStatus, LocalDateTime createdAt) {
        this.car = car;
        this.client = client;
        this.startOfRentalTime = startOfRentalTime;
        this.endOfRentalTime = endOfRentalTime;
        this.carReservationStatus = carReservationStatus;
        this.createdAt = createdAt;
    }
}
