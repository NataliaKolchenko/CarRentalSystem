package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.exception.BrandAlreadyExistsException;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.service.interfaces.BrandServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/brandAndModel")
public class BrandModelController {
    private final BrandServiceInterface brandModelService;

    @Autowired
    public BrandModelController(BrandServiceInterface brandModelService) {
        this.brandModelService = brandModelService;
    }

    @PostMapping("/createNewBrand")
    public ResponseEntity<?> createNewBrand( @RequestBody  @Valid Brand newBrand) {
        try {
            brandModelService.createVehicleBrand(newBrand.getBrandName());
            return ResponseEntity.ok(brandModelService.getVehicleBrandByName(newBrand.getBrandName()));
        } catch (BrandAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 Conflict
        }
    }

    @GetMapping("/getBrandById/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable Long id){
        return ResponseEntity.ok(brandModelService.getVehicleBrandById(id));
    }
}
