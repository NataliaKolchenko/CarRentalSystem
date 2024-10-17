package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.Branch;
import com.example.CarRentalSystem.service.interfaces.BranchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/branch")
public class BranchController {
    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping("/createNewBranch")
    public ResponseEntity<Branch> createNewBranch(@RequestBody @Valid Branch branch){
        branchService.create(branch);
        return ResponseEntity.ok(branchService.getByName(branch.getBranchName()));
    }
}
