package com.example.CarRentalSystem.repository.brand;

import com.example.CarRentalSystem.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    Brand createVehicleBrand (String brandName);
    Brand updateVehicleBrand (Brand updatedBrand);
    boolean deleteVehicleBrandById(Long brandId);
    Optional<Brand> getVehicleBrandById(Long brandId);
    Brand getVehicleBrandByName(String brandName);
    List<Brand> getAllVehicleBrand();
    boolean existsById(Long brandId);
}