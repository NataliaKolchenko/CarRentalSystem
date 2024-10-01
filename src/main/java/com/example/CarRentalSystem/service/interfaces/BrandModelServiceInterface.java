package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.Brand;

import java.util.List;

public interface BrandModelServiceInterface {
    Brand createVehicleBrand (String brandName);
    Brand updateVehicleBrand (Long brandId, String newBrandName);
    boolean deleteVehicleBrandById(Long brandId);
    Brand getVehicleVehicleBrandById(Long brandId);
    List<Brand> getAllVehicleBrand();
}
