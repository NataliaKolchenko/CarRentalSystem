package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.service.interfaces.BrandServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/BrandAndModel")
public class BrandModelController {
    private final BrandServiceInterface brandModelService;

    @Autowired
    public BrandModelController(BrandServiceInterface brandModelService) {
        this.brandModelService = brandModelService;
    }

    @PostMapping("/createNewBrand")
    public ResponseEntity<Brand> createNewBrand( @RequestBody  @Valid Brand newBrand) {
        brandModelService.createVehicleBrand(newBrand.getBrandName());
        return ResponseEntity.ok(brandModelService.getVehicleVehicleBrandByName(newBrand.getBrandName()));

    }
}
