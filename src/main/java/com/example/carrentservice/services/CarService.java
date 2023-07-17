package com.example.carrentservice.services;

import com.example.carrentservice.entities.Car;
import com.example.carrentservice.exceptions.IncorrectIdentifierException;
import com.example.carrentservice.repositories.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CarService {

    private final CarRepository carRepository;


    public Car saveNewCar(Car car) {
        car.setId(null);
        return carRepository.save(car);
    }

    public Car updateCar(Car car) {
        if (car.getId() != null) {
            carRepository.findById(car.getId())
                    .orElseThrow(() -> new IncorrectIdentifierException(
                            String.format("ERROR: Car with ID=%d doesn't exists", car.getId()))
                    );
            return carRepository.save(car);
        } else {
            throw new IncorrectIdentifierException(
                    "ERROR: Car ID is NULL");
        }
    }

    public Car findCarById(Long id){
        Optional<Car> byId = carRepository.findById(id);
        return byId.orElseThrow(() -> new IncorrectIdentifierException(
                String.format("ERROR: Car with ID=%d doesn't exists", id)));
    }


}
