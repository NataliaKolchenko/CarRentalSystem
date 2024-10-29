package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaVehicleRepository extends JpaRepository<Vehicle, Long> {
}
