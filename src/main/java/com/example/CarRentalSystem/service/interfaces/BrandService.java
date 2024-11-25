package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.Brand;

import java.util.List;

public interface BrandService {
    Brand create(Brand brand);
    Brand update(Long brandId, String newBrandName);
    void deleteById(Long brandId);
    Brand getById(Long brandId);
    Brand getByName(String brandName);
    List<Brand> getAllBrands();
}
