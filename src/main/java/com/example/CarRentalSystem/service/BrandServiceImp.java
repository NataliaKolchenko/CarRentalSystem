package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.exception.BrandAlreadyExistsException;
import com.example.CarRentalSystem.exception.BrandNotFoundException;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.repository.brand.BrandRepository;
import com.example.CarRentalSystem.service.interfaces.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class BrandServiceImp implements BrandService {
    private final BrandRepository brandRepository;

    @Autowired
    public BrandServiceImp(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Brand createVehicleBrand(String brandName) {
        Brand checkExistBrand = brandRepository.getVehicleBrandByName(brandName);
        if(checkExistBrand != null) {
            throw new BrandAlreadyExistsException("BrandName has to be unique");
        }
       Brand newBrand = brandRepository.createVehicleBrand(brandName);
        return newBrand;
    }

    @Override
    public Brand updateVehicleBrand(Long brandId, String newBrandName) {
        Brand brand = getVehicleBrandById(brandId);
       if (brandRepository.getVehicleBrandByName(newBrandName) != null){
           throw new BrandAlreadyExistsException("BrandName has to be unique");
       }
        brand.setBrandName(newBrandName);
        Brand updatedBrand = brandRepository.updateVehicleBrand(brand);
        return updatedBrand;
    }

    @Override
    public boolean deleteVehicleBrandById(Long brandId) {
        if (!brandRepository.existsById(brandId)) {
            throw new BrandNotFoundException("BrandId was not found");
        }
        return brandRepository.deleteVehicleBrandById(brandId);
    }

    @Override
    public Brand getVehicleBrandById(Long brandId) {
        Optional<Brand> brandOpt = brandRepository.getVehicleBrandById(brandId);
        Brand brand = brandOpt.orElseThrow(() -> new BrandNotFoundException("BrandId was not found"));
        return brand;
    }

    @Override
    public Brand getVehicleBrandByName(String brandName) {
        Brand brand = brandRepository.getVehicleBrandByName(brandName);
        if (brand == null) {
            throw new BrandNotFoundException("BrandName was not found");
        }
        return brand;
    }

    @Override
    public List<Brand> getAllVehicleBrand() {
        List<Brand> brandList = brandRepository.getAllVehicleBrand();
        return brandList.isEmpty() ? Collections.emptyList() : brandList;
    }

}
