package com.example.carrentservice.reservation;

import com.example.carrentservice.car.Car;
import com.example.carrentservice.client.Client;
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
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_ID")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull(message = "Invalid start of rental time: null")
    private LocalDate startOfRentalTime;

    @NotNull(message = "Invalid end of rental time: null")
    private LocalDate endOfRentalTime;

    private CarReservationStatus carReservationStatus;

    private LocalDateTime createdAt;

}
