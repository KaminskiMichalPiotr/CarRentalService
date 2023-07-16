package com.example.carrentservice.reservation;

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
    public List<CarReservation> getCarReservations(){
        return Collections.emptyList();
    }

    @PostMapping
    public ResponseEntity<CarReservation> createReservation(@Valid @RequestBody CarReservation carReservation){
        CarReservation reservation = carReservationService.createCarReservation(carReservation);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
}
