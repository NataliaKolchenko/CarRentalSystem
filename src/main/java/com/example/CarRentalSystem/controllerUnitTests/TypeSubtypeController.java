package com.example.CarRentalSystem.controllerUnitTests;

import com.example.CarRentalSystem.model.SubType;
import com.example.CarRentalSystem.model.VehicleType;
import com.example.CarRentalSystem.service.interfaces.SubTypeService;
import com.example.CarRentalSystem.service.interfaces.VehicleTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/typeAndSubtype")
public class TypeSubtypeController {
    private final VehicleTypeService typeService;
    private final SubTypeService subTypeService;

    public TypeSubtypeController(VehicleTypeService typeService, SubTypeService subTypeService) {
        this.typeService = typeService;
        this.subTypeService = subTypeService;
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

    // -----------------------------------------------------------------

    @PostMapping("/createNewSubType")
    public ResponseEntity<SubType> createNewSubType (@RequestBody @Valid SubType newSubType){
        subTypeService.create(newSubType);
        return ResponseEntity.ok(subTypeService.getByName(newSubType.getSubTypeName()));
    }

    @GetMapping("/getSubTypeById/{id}")
    public ResponseEntity<SubType> getSubTypeById(@PathVariable Long id){
        return ResponseEntity.ok(subTypeService.getById(id));
    }

    @GetMapping("/getAllSubTypes")
    public ResponseEntity<List<SubType>> getAllSubTypes(){
        return ResponseEntity.ok(subTypeService.getAllSubTypes());
    }

    @DeleteMapping("/deleteSubTypeById/{id}")
    public ResponseEntity<Boolean> deleteSubTypeById(@PathVariable Long id){
        subTypeService.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/updateSubType/{id}")
    public ResponseEntity<SubType> updateSubType (@PathVariable Long id,
                                                  @RequestBody @Valid SubType newSubType){
        return ResponseEntity.ok(subTypeService.update(id, newSubType.getSubTypeName()));
    }
}
