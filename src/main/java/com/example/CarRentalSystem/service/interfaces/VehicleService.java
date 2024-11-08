package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.model.dto.VehicleRequestDto;

import java.util.List;

public interface VehicleService {
    Vehicle create(VehicleRequestDto vehicleRequestDto);
    Vehicle getById(Long id);
    List<Vehicle> getAllVehicles();
    Vehicle update(Long id, VehicleRequestDto vehicleDto);
    List<Vehicle> getFavoriteVehicles();

    Boolean addToFavorites(Long id);
    Boolean removeFromFavorites(Long id);
}
