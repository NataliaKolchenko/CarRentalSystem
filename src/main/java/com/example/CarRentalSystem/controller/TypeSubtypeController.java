package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.model.VehicleType;
import com.example.CarRentalSystem.service.interfaces.VehicleTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/typeAndSubtype")
public class TypeSubtypeController {
    private final VehicleTypeService typeService;

    public TypeSubtypeController(VehicleTypeService typeService) {
        this.typeService = typeService;
    }

    @PostMapping("/createNewType")
    public ResponseEntity<VehicleType> createNewType(@RequestBody @Valid VehicleType newType) {
        typeService.create(newType.getVehicleTypeName());
        return ResponseEntity.ok(typeService.getByName(newType.getVehicleTypeName()));
    }
}
