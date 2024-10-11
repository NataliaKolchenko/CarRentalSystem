package com.example.CarRentalSystem.repository.brand;

import com.example.CarRentalSystem.model.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BrandRepositoryImp implements BrandRepository {
    private final JpaBrandRepository jpaBrandRepository;
    @Override
    public Brand createVehicleBrand(String brandName) {
        Brand brand = new Brand(brandName);
        return jpaBrandRepository.save(brand);
    }

    @Override
    public Brand updateVehicleBrand(Brand brand) {
        return jpaBrandRepository.save(brand);
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

    @Override
    public boolean existsById(Long brandId) {
        return jpaBrandRepository.existsById(brandId);
    }
}
