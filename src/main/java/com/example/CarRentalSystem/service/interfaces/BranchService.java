package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.entity.Branch;

import java.util.List;

public interface BranchService {
    Branch create (Branch branch);
    Branch getById(Long branchId);
    Branch getByName(String branchName);
    List<Branch> getAllBranches();
}
