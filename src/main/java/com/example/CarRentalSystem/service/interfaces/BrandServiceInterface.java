package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.Brand;

import java.util.List;

public interface BrandServiceInterface {
    Brand createVehicleBrand (String brandName);
    Brand updateVehicleBrand (Long brandId, String newBrandName);
    boolean deleteVehicleBrandById(Long brandId);
    Brand getVehicleVehicleBrandById(Long brandId);
    Brand getVehicleVehicleBrandByName(String brandName);

    List<Brand> getAllVehicleBrand();
}
