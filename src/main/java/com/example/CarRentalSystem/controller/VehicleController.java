package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.model.dto.VehicleRequestDto;
import com.example.CarRentalSystem.service.interfaces.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getVehicleById/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id){
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @GetMapping("/getAllVehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles(){
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @PutMapping("/updateVehicle/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id,
                                                 @RequestBody @Valid VehicleRequestDto newVehicleDto){
        return ResponseEntity.ok(vehicleService.update(id, newVehicleDto));
    }
}
