package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBookingRepository extends JpaRepository<Booking, Long> {
}
