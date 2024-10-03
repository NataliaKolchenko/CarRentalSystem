package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.repository.interfaces.BrandRepositoryInterface;
import com.example.CarRentalSystem.repository.interfaces.JpaBrandRepositoryInterface;
import com.example.CarRentalSystem.service.interfaces.BrandServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImp implements BrandServiceInterface {
    private final BrandRepositoryInterface brandRepository;

    @Autowired
    public BrandServiceImp(BrandRepositoryInterface brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Brand createVehicleBrand(String brandName) {
       Brand newBrand = brandRepository.createVehicleBrand(brandName);
        return newBrand;
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
    public Brand getVehicleVehicleBrandByName(String brandName) {
        Brand brand = brandRepository.getVehicleVehicleBrandByName(brandName);
        return brand;
    }

    @Override
    public List<Brand> getAllVehicleBrand() {
        return null;
    }
}
