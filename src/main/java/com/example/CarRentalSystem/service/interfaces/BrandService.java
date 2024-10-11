package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.Brand;

import java.util.List;

public interface BrandService {
    Brand createVehicleBrand (String brandName);
    Brand updateVehicleBrand (Long brandId, String newBrandName);
    boolean deleteVehicleBrandById(Long brandId);
    Brand getVehicleBrandById(Long brandId);
    Brand getVehicleBrandByName(String brandName);
    List<Brand> getAllVehicleBrand();
}
