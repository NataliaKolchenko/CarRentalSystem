package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaVehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByVinCodeAndVehicleNumber(String vinCode, String vehicleNumber);
}
