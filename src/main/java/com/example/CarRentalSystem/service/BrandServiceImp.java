package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.exception.BrandAlreadyExistsException;
import com.example.CarRentalSystem.exception.BrandListIsEmptyException;
import com.example.CarRentalSystem.exception.BrandNotFoundException;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.repository.interfaces.BrandRepositoryInterface;
import com.example.CarRentalSystem.service.interfaces.BrandServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class BrandServiceImp implements BrandServiceInterface {
    private final BrandRepositoryInterface brandRepository;

    @Autowired
    public BrandServiceImp(BrandRepositoryInterface brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Brand createVehicleBrand(String brandName) {
        Brand checkExistBrand = brandRepository.getVehicleBrandByName(brandName);
        if(checkExistBrand != null && checkExistBrand.getBrandName().equals(brandName)) {
            throw new BrandAlreadyExistsException("BrandName has to be unique");
        }
       Brand newBrand = brandRepository.createVehicleBrand(brandName);
        return newBrand;
    }

    @Override
    public Brand updateVehicleBrand(Long brandId, String newBrandName) {
        return null;
    }

    @Override
    public boolean deleteVehicleBrandById(Long brandId) {
//        Brand brand = Optional.of(brandRepository.getVehicleBrandById(brandId));
        return false;
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
            throw new BrandNotFoundException("BrandId was not found");
        }
        return brand;
    }

    @Override
    public List<Brand> getAllVehicleBrand() {
        List<Brand> brandList = brandRepository.getAllVehicleBrand();
        if (brandList.isEmpty()){
            throw new BrandListIsEmptyException("BrandList is empty");
        }
        return brandList;
    }
}
