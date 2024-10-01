package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.RequestResponseModel.ResponseAPI;
import com.example.CarRentalSystem.service.interfaces.BrandModelServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/BrandAndModel")
public class BrandModelController {
    private ResponseAPI responseAPI;
    private final BrandModelServiceInterface brandModelService;

    @Autowired
    public BrandModelController(BrandModelServiceInterface brandModelService) {
        this.brandModelService = brandModelService;
    }
}
