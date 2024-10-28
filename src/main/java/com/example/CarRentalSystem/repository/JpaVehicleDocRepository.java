package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.model.VehicleDoc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaVehicleDocRepository extends JpaRepository<VehicleDoc, Long> {
}
