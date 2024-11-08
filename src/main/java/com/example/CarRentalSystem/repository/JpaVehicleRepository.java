package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaVehicleRepository extends JpaRepository<Vehicle, Long>, SearchRepository {
    List<Vehicle> findByVinCodeAndVehicleNumber(String vinCode, String vehicleNumber);

    @Query("SELECT v FROM Vehicle v where v.favorite = true")
    List<Vehicle> findByFavorite();
}
