package com.example.carrentservice.controllers;

import com.example.carrentservice.entities.Car;
import com.example.carrentservice.exceptions.ErrorResponse;
import com.example.carrentservice.services.CarService;
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

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/car")
public class CarController {

    private final CarService carService;

    @PostMapping
    @Operation(summary = "Create a new car")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public ResponseEntity<Car> saveNewCar(@Valid @RequestBody Car car){
        return new ResponseEntity<>(carService.saveNewCar(car), HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "Update car details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Incorrect car ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public ResponseEntity<Car> updateCar(@Valid @RequestBody Car car){
        return new ResponseEntity<>(carService.updateCar(car), HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Operation(summary = "Find car by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Incorrect car ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public ResponseEntity<Car> getCarById(@PathVariable Long id){
        return new ResponseEntity<>(carService.findCarById(id), HttpStatus.OK);
    }


}
