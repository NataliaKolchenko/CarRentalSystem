package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.VehicleType;

import java.util.List;

public interface VehicleTypeService {
    VehicleType create (String vehicleTypeName);
    VehicleType update (Long vehicleTypeId, String newVehicleTypeName);
    void deleteById(Long vehicleTypeId);
    VehicleType getById(Long vehicleTypeId);
    VehicleType getByName(String vehicleTypeName);
    List<VehicleType> getAll();
}
