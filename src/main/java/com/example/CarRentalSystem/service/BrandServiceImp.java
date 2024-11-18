package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.repository.JpaBrandRepository;
import com.example.CarRentalSystem.service.interfaces.BrandService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service

public class BrandServiceImp implements BrandService {
    private final JpaBrandRepository brandRepository;
    private final ModelServiceImp modelService;
    public BrandServiceImp(JpaBrandRepository brandRepository, @Lazy ModelServiceImp modelService) {
        this.brandRepository = brandRepository;
        this.modelService = modelService;
    }

    @Override
    public Brand create(String brandName) {
        Brand checkExistBrand = brandRepository.findByBrandName(brandName);
        if(checkExistBrand != null) {
            throw new SubjectAlreadyExistsException(ErrorMessage.BRAND_NAME_IS_ALREADY_EXIST);
        }
       Brand newBrand = new Brand(brandName);

        return brandRepository.save(newBrand);
    }

    @Override
    public Brand update(Long brandId, String newBrandName) {
        Brand brand = getById(brandId);
       if (brandRepository.findByBrandName(newBrandName) != null){
           throw new SubjectAlreadyExistsException(ErrorMessage.BRAND_NAME_IS_ALREADY_EXIST);
       }
        brand.setBrandName(newBrandName);
        return brandRepository.save(brand);
    }

    @Override
    public void deleteById(Long brandId) {
        if (!brandRepository.existsById(brandId)) {
            throw new SubjectNotFoundException(ErrorMessage.BRAND_ID_WAS_NOT_FOUND);
        }

        if (modelService.existsByBrandId(brandId)) {
            throw new SubjectNotFoundException(ErrorMessage.CANNOT_DELETE_BRAND);
        } else {
            brandRepository.deleteById(brandId);
        }
    }

    @Override
    public Brand getById(Long brandId) {
        Optional<Brand> brandOpt = brandRepository.findById(brandId);
        Brand brand = brandOpt.orElseThrow(
                () -> new SubjectNotFoundException(ErrorMessage.BRAND_ID_WAS_NOT_FOUND));
        return brand;
    }

    @Override
    public Brand getByName(String brandName) {
        Brand brand = brandRepository.findByBrandName(brandName);
        if (brand == null) {
            throw new SubjectNotFoundException(ErrorMessage.BRAND_NAME_WAS_NOT_FOUND);
        }
        return brand;
    }

    @Override
    public List<Brand> getAllBrands() {
        List<Brand> brandList = brandRepository.findAll();
        return brandList.isEmpty() ? Collections.emptyList() : brandList;
    }

}
