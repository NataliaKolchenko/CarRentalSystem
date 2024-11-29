package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.model.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaVehicleTypeRepository extends JpaRepository<VehicleType, Long> {
    VehicleType findByVehicleTypeName(String vehicleTypeName);
}
