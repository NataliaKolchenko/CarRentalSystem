package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.entity.Branch;
import com.example.CarRentalSystem.service.interfaces.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/branch")
@Tag(name = "Branch Controller", description = "Branch Controller is available only to users with the \"DIRECTOR\" role")
public class BranchController {
    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @Operation(summary = "Create a new branch")
    @PostMapping("/createNewBranch")
    public ResponseEntity<Branch> createNewBranch(@RequestBody @Valid Branch branch){
        branchService.create(branch);
        return ResponseEntity.ok(branchService.getByName(branch.getBranchName()));
    }

    @Operation(summary = "Get information about branch by ID")
    @GetMapping("/getBranchById/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id){
        return ResponseEntity.ok(branchService.getById(id));
    }

    @Operation(summary = "Get all branches list")
    @GetMapping("/getAllBranches")
    public ResponseEntity<List<Branch>> gelAllBranches(){
        return ResponseEntity.ok(branchService.getAllBranches());
    }
}
