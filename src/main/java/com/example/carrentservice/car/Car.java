package com.example.carrentservice.car;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String manufacturer;

    private int numberOfSeats;

    private CarType carType;

    private int yearOfProduction;


    public Car(String manufacturer, int numberOfSeats, CarType carType) {
        this.manufacturer = manufacturer;
        this.numberOfSeats = numberOfSeats;
        this.carType = carType;
    }

}
