package com.example.CarRentalSystem.repository.interfaces;

import com.example.CarRentalSystem.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepositoryInterface {
    Brand createVehicleBrand (String brandName);
    Brand updateVehicleBrand (Brand updatedBrand);
    boolean deleteVehicleBrandById(Long brandId);
    Optional<Brand> getVehicleBrandById(Long brandId);
    Brand getVehicleBrandByName(String brandName);

    List<Brand> getAllVehicleBrand();

    boolean existsById(Long brandId);
}
