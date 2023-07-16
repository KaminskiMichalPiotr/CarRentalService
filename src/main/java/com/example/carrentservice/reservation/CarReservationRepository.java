package com.example.carrentservice.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarReservationRepository extends JpaRepository<CarReservation, Long> {
}
