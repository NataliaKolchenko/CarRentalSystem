package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.service.interfaces.BrandServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/BrandAndModel")
public class BrandModelController {
    private final BrandServiceInterface brandModelService;

    @Autowired
    public BrandModelController(BrandServiceInterface brandModelService) {
        this.brandModelService = brandModelService;
    }
}
