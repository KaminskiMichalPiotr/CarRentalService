package com.example.carrentservice.car;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/car")
public class CarController {

    @GetMapping
    public Car getCarById(){
        return new Car("Skoda", 5, CarType.HATCHBACK);
    }

}
