package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.model.dto.VehicleRequestDto;

public interface VehicleService {
    Vehicle create(VehicleRequestDto vehicleRequestDto);
}
