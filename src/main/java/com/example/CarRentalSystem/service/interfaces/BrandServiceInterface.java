package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandServiceInterface {
    Brand createVehicleBrand (String brandName);
    Brand updateVehicleBrand (Long brandId, String newBrandName);
    boolean deleteVehicleBrandById(Long brandId);
    Optional<Brand> getVehicleBrandById(Long brandId);
    Brand getVehicleBrandByName(String brandName);

    List<Brand> getAllVehicleBrand();
}
