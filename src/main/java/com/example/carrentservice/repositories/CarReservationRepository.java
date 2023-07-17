package com.example.carrentservice.repositories;

import com.example.carrentservice.entities.CarReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarReservationRepository extends JpaRepository<CarReservation, Long> {

    List<CarReservation> findCarReservationByStartOfRentalTimeIsBetween(LocalDate start, LocalDate stop);

    List<CarReservation> findCarReservationByEndOfRentalTimeIsBetween(LocalDate start, LocalDate stop);


}
