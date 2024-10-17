package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.model.Branch;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.repository.JpaBranchRepository;
import com.example.CarRentalSystem.service.interfaces.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class BranchServiceImp implements BranchService {
    private final JpaBranchRepository branchRepository;

    @Autowired
    public BranchServiceImp(JpaBranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public Branch create(Branch branch) {
        Branch checkExistBranch = branchRepository.findByBranchName(branch.getBranchName());
        if (checkExistBranch != null) {
            throw new SubjectAlreadyExistsException("BranchName has to be unique");
        }
        Branch newBranch = Branch.builder()
                .branchName(branch.getBranchName())
                .address(branch.getAddress())
                .phone(branch.getPhone())
                .workingTime(branch.getWorkingTime())
                .build();

        branchRepository.save(newBranch);
        return newBranch;
    }

    @Override
    public Branch getById(Long branchId) {
        return null;
    }

    @Override
    public Branch getByName(String branchName) {
        return null;
    }

    @Override
    public List<Branch> getAllBranches() {
        return null;
    }
}
