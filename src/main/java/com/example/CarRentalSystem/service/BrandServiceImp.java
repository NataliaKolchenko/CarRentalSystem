package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.repository.JpaBrandRepository;
import com.example.CarRentalSystem.service.interfaces.BrandService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class BrandServiceImp implements BrandService {
    private final JpaBrandRepository brandRepository;
    public BrandServiceImp(JpaBrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Brand createVehicleBrand(String brandName) {
        Brand checkExistBrand = brandRepository.findByBrandName(brandName);
        if(checkExistBrand != null) {
            throw new SubjectAlreadyExistsException(ErrorMessage.BRAND_NAME_IS_ALREADY_EXIST);
        }
       Brand newBrand = new Brand(brandName);

        return brandRepository.save(newBrand);
    }

    @Override
    public Brand updateVehicleBrand(Long brandId, String newBrandName) {
        Brand brand = getVehicleBrandById(brandId);
       if (brandRepository.findByBrandName(newBrandName) != null){
           throw new SubjectAlreadyExistsException(ErrorMessage.BRAND_NAME_IS_ALREADY_EXIST);
       }
        brand.setBrandName(newBrandName);
        return brandRepository.save(brand);
    }

    @Override
    public void deleteVehicleBrandById(Long brandId) {
        if (!brandRepository.existsById(brandId)) {
            throw new SubjectNotFoundException(ErrorMessage.BRAND_ID_WAS_NOT_FOUND);
        }
        brandRepository.deleteById(brandId);
    }

    @Override
    public Brand getVehicleBrandById(Long brandId) {
        Optional<Brand> brandOpt = brandRepository.findById(brandId);
        Brand brand = brandOpt.orElseThrow(() -> new SubjectNotFoundException(ErrorMessage.BRAND_ID_WAS_NOT_FOUND));
        return brand;
    }

    @Override
    public Brand getVehicleBrandByName(String brandName) {
        Brand brand = brandRepository.findByBrandName(brandName);
        if (brand == null) {
            throw new SubjectNotFoundException(ErrorMessage.BRAND_NAME_WAS_NOT_FOUND);
        }
        return brand;
    }

    @Override
    public List<Brand> getAllVehicleBrand() {
        List<Brand> brandList = brandRepository.findAll();
        return brandList.isEmpty() ? Collections.emptyList() : brandList;
    }

}
