package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.repository.interfaces.BrandRepositoryInterface;
import com.example.CarRentalSystem.repository.interfaces.JpaBrandRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BrandRepositoryImp implements BrandRepositoryInterface {
    private final JpaBrandRepositoryInterface jpaBrandRepository;
    @Override
    public Brand createVehicleBrand(String brandName) {
        Brand brand = new Brand(brandName);
        return jpaBrandRepository.save(brand);
    }

    @Override
    public Brand updateVehicleBrand(Long brandId, String newBrandName) {
        return null;
    }

    @Override
    public boolean deleteVehicleBrandById(Long brandId) {
        jpaBrandRepository.deleteById(brandId);
        return true;
    }





    @Override
    public Optional<Brand> getVehicleBrandById(Long brandId) {
        return jpaBrandRepository.findById(brandId);
    }

    @Override
    public Brand getVehicleBrandByName(String brandName) {
        return jpaBrandRepository.findByBrandName(brandName);
    }

    @Override
    public List<Brand> getAllVehicleBrand() {return jpaBrandRepository.findAll();
    }
}
