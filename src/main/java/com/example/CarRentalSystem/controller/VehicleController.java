package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.model.dto.VehicleRequestDto;
import com.example.CarRentalSystem.service.interfaces.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/createVehicle")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody @Valid VehicleRequestDto vehicleRequestDto){

        return ResponseEntity.ok(vehicleService.create(vehicleRequestDto));
    }
}
