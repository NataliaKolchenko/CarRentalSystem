package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.entity.Brand;
import com.example.CarRentalSystem.model.entity.Model;
import com.example.CarRentalSystem.service.interfaces.BrandService;
import com.example.CarRentalSystem.service.interfaces.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Validated
@RestController
@RequestMapping("/brandAndModel")
@Tag(name = "Brand and Model Controller", description = "Brand and Model Controller is available only to users with the \"ADMIN\" role")
public class BrandModelController {
    private final BrandService brandService;
    private final ModelService modelService;

    public BrandModelController(BrandService brandService, ModelService modelService) {
        this.brandService = brandService;
        this.modelService = modelService;
    }

    @Operation(summary = "Create a new brand of vehicle")
    @PostMapping("/createNewBrand")
    public ResponseEntity<Brand> createNewBrand( @RequestBody  @Valid Brand newBrand) {
            brandService.create(newBrand);
            return ResponseEntity.ok(brandService.getByName(newBrand.getBrandName()));
    }

    @Operation(summary = "Get information about brand by ID")
    @GetMapping("/getBrandById/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Long id){
            return ResponseEntity.ok(brandService.getById(id));
    }

    @Operation(summary = "Get all brands list")
    @GetMapping("/getAllBrands")
    public ResponseEntity<List<Brand>> getAllBrands(){
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    @Operation(summary = "Delete brand by ID")
    @DeleteMapping("/deleteBrandById/{id}")
    public ResponseEntity<Boolean> deleteBrandById(@PathVariable Long id){
        brandService.deleteById(id);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Update information about brand by BrandID")
    @PutMapping("/updateBrand/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Long id,
                                             @RequestBody @Valid Brand newBrand){
        return ResponseEntity.ok(brandService.update(id, newBrand.getBrandName()));
    }

    // -----------------------------------------------------------------

    @Operation(summary = "Create a new model")
    @PostMapping("/createNewModel")
    public ResponseEntity<Model> createNewModel(@RequestBody  @Valid Model newModel) {
        modelService.create(newModel);
        return ResponseEntity.ok(modelService.getByName(newModel.getModelName()));
    }

    @Operation(summary = "Get information about model by ID")
    @GetMapping("/getModelById/{id}")
    public ResponseEntity<Model> getModelById(@PathVariable Long id){
        return ResponseEntity.ok(modelService.getById(id));
    }

    @Operation(summary = "Get all models list")
    @GetMapping("/getAllModels")
    public ResponseEntity<List<Model>> getAllModels(){
        return ResponseEntity.ok(modelService.getAllModels());
    }

    @Operation(summary = "Delete brand by ID")
    @DeleteMapping("/deleteModelById/{id}")
    public ResponseEntity<Boolean> deleteModelById(@PathVariable Long id){
        modelService.deleteById(id);
        return ResponseEntity.ok(true);
    }
    @Operation(summary = "Update information about brand by BrandID")
    @PutMapping("/updateModel/{id}")
    public ResponseEntity<Model> updateModel(@PathVariable Long id,
                                             @RequestBody @Valid Model newModel){
        return ResponseEntity.ok(modelService.update(id, newModel.getModelName()));
    }
}
