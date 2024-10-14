package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.model.Model;
import com.example.CarRentalSystem.service.interfaces.BrandService;
import com.example.CarRentalSystem.service.interfaces.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/brandAndModel")
public class BrandModelController {
    private final BrandService brandService;
    private final ModelService modelService;

    @Autowired
    public BrandModelController(BrandService brandService, ModelService modelService) {
        this.brandService = brandService;
        this.modelService = modelService;
    }

    @PostMapping("/createNewBrand")
    public ResponseEntity<Brand> createNewBrand( @RequestBody  @Valid Brand newBrand) {
            brandService.createVehicleBrand(newBrand.getBrandName());
            return ResponseEntity.ok(brandService.getVehicleBrandByName(newBrand.getBrandName()));
    }

    @GetMapping("/getBrandById/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Long id){
            return ResponseEntity.ok(brandService.getVehicleBrandById(id));
    }

    @GetMapping("/getAllBrands")
    public ResponseEntity<List<Brand>> getAllBrands(){
        return ResponseEntity.ok(brandService.getAllVehicleBrand());
    }

    @DeleteMapping("/deleteBrandById/{id}")
    public ResponseEntity<Boolean> deleteBrandById(@PathVariable Long id){
        brandService.deleteVehicleBrandById(id);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/updateBrand/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Long id,
                                             @RequestBody @Valid Brand newBrand){
        return ResponseEntity.ok(brandService.updateVehicleBrand(id, newBrand.getBrandName()));
    }

    // -----------------------------------------------------------------

    @PostMapping("/createNewModel")
    public ResponseEntity<Model> createNewModel(@RequestBody  @Valid Model newModel) {
        modelService.createModel(newModel);
        return ResponseEntity.ok(modelService.getModelByName(newModel.getModelName()));
    }

    @GetMapping("/getModelById/{id}")
    public ResponseEntity<Model> getModelById(@PathVariable Long id){
        return ResponseEntity.ok(modelService.getModelById(id));
    }

    @GetMapping("/getAllModels")
    public ResponseEntity<List<Model>> getAllModels(){
        return ResponseEntity.ok(modelService.getAllModels());
    }

    @DeleteMapping("/deleteModelById/{id}")
    public ResponseEntity<Boolean> deleteModelById(@PathVariable Long id){
        modelService.deleteModelById(id);
        return ResponseEntity.ok(true);
    }
    @PutMapping("/updateModel/{id}")
    public ResponseEntity<Model> updateModel(@PathVariable Long id,
                                             @RequestBody @Valid Model newModel){
        return ResponseEntity.ok(modelService.updateModel(id, newModel.getModelName()));
    }
}
