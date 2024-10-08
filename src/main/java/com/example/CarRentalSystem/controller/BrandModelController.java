package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.exception.BrandAlreadyExistsException;
import com.example.CarRentalSystem.exception.BrandNotFoundException;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.service.interfaces.BrandServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/brandAndModel")
public class BrandModelController {
    private final BrandServiceInterface brandModelService;

    @Autowired
    public BrandModelController(BrandServiceInterface brandModelService) {
        this.brandModelService = brandModelService;
    }

    @PostMapping("/createNewBrand")
    public ResponseEntity<Brand> createNewBrand( @RequestBody  @Valid Brand newBrand) {
            brandModelService.createVehicleBrand(newBrand.getBrandName());
            return ResponseEntity.ok(brandModelService.getVehicleBrandByName(newBrand.getBrandName()));
    }

    @GetMapping("/getBrandById/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Long id){
            return ResponseEntity.ok(brandModelService.getVehicleBrandById(id));
    }

    @GetMapping("/getAllBrands")
    public ResponseEntity<List<Brand>> getAllBrands(){
        return ResponseEntity.ok(brandModelService.getAllVehicleBrand());
    }

    @DeleteMapping("/deleteBrandById/{id}")
    public ResponseEntity<Boolean> deleteBrandById(@PathVariable Long id){
        return ResponseEntity.ok(brandModelService.deleteVehicleBrandById(id));
    }
}
