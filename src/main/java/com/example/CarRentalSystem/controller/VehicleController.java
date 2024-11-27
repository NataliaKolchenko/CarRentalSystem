package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.model.dto.VehicleRequestDto;
import com.example.CarRentalSystem.service.interfaces.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/vehicle")
@Tag(name = "Vehicle Controller", description = "Vehicle Controller is available only to users with the \"ADMIN\" role")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(summary = "Create a new vehicle")
    @PostMapping("/createVehicle")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody @Valid VehicleRequestDto vehicleRequestDto){
        return ResponseEntity.ok(vehicleService.create(vehicleRequestDto));
    }

    @Operation(summary = "Get a vehicle by ID")
    @GetMapping("/getVehicleById/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id){
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @Operation(summary = "Get all vehicle list")
    @GetMapping("/getAllVehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles(){
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @Operation(summary = "Update a vehicle by vehicleId")
    @PutMapping("/updateVehicle/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id,
                                                 @RequestBody @Valid VehicleRequestDto newVehicleDto){
        return ResponseEntity.ok(vehicleService.update(id, newVehicleDto));
    }

    @Operation(summary = "Get your favorite vehicle list")
    @GetMapping("/getFavoriteVehicles")
    public ResponseEntity<List<Vehicle>> getFavoriteVehicles(){
        return ResponseEntity.ok(vehicleService.getFavoriteVehicles());
    }

    @Operation(summary = "Add a vehicle to your favorite list")
    @PutMapping("/addToFavorites")
    public ResponseEntity<Boolean> addToFavorites(@RequestBody @Valid Long id){
        return ResponseEntity.ok(vehicleService.addToFavorites(id));
    }

    @Operation(summary = "Remove a vehicle to your favorite list")
    @PutMapping("/removeToFavorites")
    public ResponseEntity<Boolean> removeToFavorites(@RequestBody @Valid Long id){
        return ResponseEntity.ok(vehicleService.removeFromFavorites(id));
    }
}
