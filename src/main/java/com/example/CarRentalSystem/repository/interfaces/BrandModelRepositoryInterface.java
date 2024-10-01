package com.example.CarRentalSystem.repository.interfaces;

import com.example.CarRentalSystem.model.Brand;

import java.util.List;

public interface BrandModelRepositoryInterface {
    Brand createVehicleBrand (String brandName);
    Brand updateVehicleBrand (Long brandId, String newBrandName);
    boolean deleteVehicleBrandById(Long brandId);
    Brand getVehicleVehicleBrandById(Long brandId);
    List<Brand> getAllVehicleBrand();
}
