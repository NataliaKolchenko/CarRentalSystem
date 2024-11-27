package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.SubType;
import com.example.CarRentalSystem.model.VehicleType;
import com.example.CarRentalSystem.service.interfaces.SubTypeService;
import com.example.CarRentalSystem.service.interfaces.VehicleTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/typeAndSubtype")
@Tag(name = "Type and Subtype Controller", description = "Type and Subtype Controller is available only to users with the \"ADMIN\" role")

public class TypeSubtypeController {
    private final VehicleTypeService typeService;
    private final SubTypeService subTypeService;

    public TypeSubtypeController(VehicleTypeService typeService, SubTypeService subTypeService) {
        this.typeService = typeService;
        this.subTypeService = subTypeService;
    }

    @Operation(summary = "Create a new type of vehicle")
    @PostMapping("/createNewType")
    public ResponseEntity<VehicleType> createNewType(@RequestBody @Valid VehicleType newType) {
        typeService.create(newType.getVehicleTypeName());
        return ResponseEntity.ok(typeService.getByName(newType.getVehicleTypeName()));
    }

    @Operation(summary = "Get information about type by ID")
    @GetMapping("/getTypeById/{id}")
    public ResponseEntity<VehicleType> getTypeById(@PathVariable Long id){
        return ResponseEntity.ok(typeService.getById(id));
    }

    @Operation(summary = "Get all types list")
    @GetMapping("/getAllTypes")
    public ResponseEntity<List<VehicleType>> getAllTypes(){
        return ResponseEntity.ok(typeService.getAll());
    }

    @Operation(summary = "Delete type by ID")
    @DeleteMapping("/deleteTypeById/{id}")
    public ResponseEntity<Boolean> deleteTypeById(@PathVariable Long id){
        typeService.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Update information about type by BrandID")
    @PutMapping("/updateType/{id}")
    public ResponseEntity<VehicleType> updateType(@PathVariable Long id,
                                                  @RequestBody @Valid VehicleType newType){
        return ResponseEntity.ok(typeService.update(id, newType.getVehicleTypeName()));
    }

    // -----------------------------------------------------------------

    @Operation(summary = "Create a new subtype")
    @PostMapping("/createNewSubType")
    public ResponseEntity<SubType> createNewSubType (@RequestBody @Valid SubType newSubType){
        subTypeService.create(newSubType);
        return ResponseEntity.ok(subTypeService.getByName(newSubType.getSubTypeName()));
    }

    @Operation(summary = "Get information about subtype by ID")
    @GetMapping("/getSubTypeById/{id}")
    public ResponseEntity<SubType> getSubTypeById(@PathVariable Long id){
        return ResponseEntity.ok(subTypeService.getById(id));
    }

    @Operation(summary = "Get all subtypes list")
    @GetMapping("/getAllSubTypes")
    public ResponseEntity<List<SubType>> getAllSubTypes(){
        return ResponseEntity.ok(subTypeService.getAllSubTypes());
    }

    @Operation(summary = "Delete subtype by ID")
    @DeleteMapping("/deleteSubTypeById/{id}")
    public ResponseEntity<Boolean> deleteSubTypeById(@PathVariable Long id){
        subTypeService.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Update information about subtype by BrandID")
    @PutMapping("/updateSubType/{id}")
    public ResponseEntity<SubType> updateSubType (@PathVariable Long id,
                                                  @RequestBody @Valid SubType newSubType){
        return ResponseEntity.ok(subTypeService.update(id, newSubType.getSubTypeName()));
    }
}
