package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.service.interfaces.BrandServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImp implements BrandServiceInterface {

    @Override
    public Brand createVehicleBrand(String brandName) {
        return null;
    }

    @Override
    public Brand updateVehicleBrand(Long brandId, String newBrandName) {
        return null;
    }

    @Override
    public boolean deleteVehicleBrandById(Long brandId) {
        return false;
    }

    @Override
    public Brand getVehicleVehicleBrandById(Long brandId) {
        return null;
    }

    @Override
    public List<Brand> getAllVehicleBrand() {
        return null;
    }
}
