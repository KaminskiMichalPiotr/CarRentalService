package com.example.carrentservice.controllers;

import com.example.carrentservice.entities.Car;
import com.example.carrentservice.entities.CarType;
import com.example.carrentservice.services.CarService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/car")
public class CarController {


    private final CarService carService;

    @PostMapping
    public ResponseEntity<Car> saveCar(@Valid @RequestBody Car car){
        return new ResponseEntity<>(carService.saveNewCar(car), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Car> updateCar(@Valid @RequestBody Car car){
        return new ResponseEntity<>(carService.updateCar(car), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id){
        return new ResponseEntity<>(carService.findCarById(id), HttpStatus.OK);
    }


}
