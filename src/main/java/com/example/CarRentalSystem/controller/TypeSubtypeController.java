package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.model.VehicleType;
import com.example.CarRentalSystem.service.interfaces.VehicleTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getTypeById/{id}")
    public ResponseEntity<VehicleType> getTypeById(@PathVariable Long id){
        return ResponseEntity.ok(typeService.getById(id));
    }

    @GetMapping("/getAllTypes")
    public ResponseEntity<List<VehicleType>> getAllTypes(){
        return ResponseEntity.ok(typeService.getAll());
    }

    @DeleteMapping("/deleteTypeById/{id}")
    public ResponseEntity<Boolean> deleteTypeById(@PathVariable Long id){
        typeService.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/updateType/{id}")
    public ResponseEntity<VehicleType> updateType(@PathVariable Long id,
                                                  @RequestBody @Valid VehicleType newType){
        return ResponseEntity.ok(typeService.update(id, newType.getVehicleTypeName()));
    }
}
