package com.example.carrentservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "ERROR: Manufacturer cannot be null")
    private String manufacturer;

    @Range(min = 2, max = 10, message = "ERROR: Number of seats should be between 2 and 10")
    private int numberOfSeats;

    @NotNull(message = "ERROR: Car type cannot be null")
    private CarType carType;

    @Positive(message = "ERROR: Year of production must be a positive value")
    @Range(min = 1900, message = "ERROR: Year of production must be greater than or equal to 1900")
    private int yearOfProduction;


    public Car(String manufacturer, int numberOfSeats, CarType carType, int yearOfProduction) {
        this.manufacturer = manufacturer;
        this.numberOfSeats = numberOfSeats;
        this.carType = carType;
        this.yearOfProduction = yearOfProduction;
    }
}
