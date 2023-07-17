package com.example.carrentservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CarReservationConfigurationDTO {

    @NotNull(message = "Invalid start of rental time: null")
    private final LocalDate startOfRentalTime;
    @NotNull(message = "Invalid end of rental time: null")
    private final LocalDate endOfRentalTime;


}
