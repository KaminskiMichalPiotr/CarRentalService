package com.example.carrentservice.controllers;

import com.example.carrentservice.dtos.CarReservationConfigurationDTO;
import com.example.carrentservice.entities.Car;
import com.example.carrentservice.entities.CarReservation;
import com.example.carrentservice.exceptions.ErrorResponse;
import com.example.carrentservice.services.CarReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/reservation")
public class CarReservationController {

    private final CarReservationService carReservationService;

    @GetMapping
    @Operation(summary = "Get all car reservations")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
    })
    public ResponseEntity<List<CarReservation>> getCarReservations() {
        return new ResponseEntity<>(carReservationService.getAllReservations(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a car reservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "422", description = "Incorrect reservation dates", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public ResponseEntity<CarReservation> createReservation(@Valid @RequestBody CarReservation carReservation) {
        //TODO: check if car and client are Valid
        CarReservation reservation = carReservationService.createCarReservation(carReservation);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "Update reservation", description = "Provided CarReservation must be valid with correct id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Incorrect car reservation ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public ResponseEntity<CarReservation> updateReservation(@Valid @RequestBody CarReservation carReservation) {
        CarReservation reservation = carReservationService.updateReservation(carReservation);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @GetMapping("/availableCars")
    @Operation(summary = "Find available cars to rent between dates",
            description = "Start date and end date of rent must be correct and cannot be mixed")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "422", description = "Incorrect reservation dates", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public ResponseEntity<List<Car>> findAvailableCars(@Valid @RequestBody CarReservationConfigurationDTO config) {
        List<Car> availableCars = carReservationService
                .findAvailableCarsByDate(config.getStartOfRentalTime(), config.getEndOfRentalTime());
        return new ResponseEntity<>(availableCars, HttpStatus.OK);
    }


}
