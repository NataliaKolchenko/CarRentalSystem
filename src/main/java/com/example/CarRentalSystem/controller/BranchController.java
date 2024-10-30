package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.Branch;
import com.example.CarRentalSystem.service.interfaces.BranchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branch")
public class BranchController {
    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping("/createNewBranch")
    public ResponseEntity<Branch> createNewBranch(@RequestBody @Valid Branch branch){
        branchService.create(branch);
        return ResponseEntity.ok(branchService.getByName(branch.getBranchName()));
    }

    @GetMapping("/getBranchById/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id){
        return ResponseEntity.ok(branchService.getById(id));
    }

    @GetMapping("/getAllBranches")
    public ResponseEntity<List<Branch>> gelAllBranches(){
        return ResponseEntity.ok(branchService.getAllBranches());
    }
}
