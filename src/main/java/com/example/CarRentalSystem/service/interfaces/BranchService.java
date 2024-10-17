package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.Branch;
import com.example.CarRentalSystem.model.Brand;

import java.util.List;

public interface BranchService {
    Branch create (Branch branch);
    Branch getById(Long branchId);
    Branch getByName(Long branchName);
    List<Branch> getAllBranches();


}
