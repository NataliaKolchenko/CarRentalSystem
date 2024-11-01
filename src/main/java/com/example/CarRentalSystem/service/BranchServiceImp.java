package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.Address;
import com.example.CarRentalSystem.model.Branch;
import com.example.CarRentalSystem.repository.JpaBranchRepository;
import com.example.CarRentalSystem.service.interfaces.BranchService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class BranchServiceImp implements BranchService {
    private final JpaBranchRepository branchRepository;
    private final AddressServiceImp addressService;
    public BranchServiceImp(JpaBranchRepository branchRepository, AddressServiceImp addressService) {
        this.branchRepository = branchRepository;
        this.addressService = addressService;
    }

    @Override
    public Branch create(Branch branch) {
        Branch checkExistBranch = branchRepository.findByBranchName(branch.getBranchName());
        if (checkExistBranch != null) {
            throw new SubjectAlreadyExistsException(ErrorMessage.BRANCH_NAME_IS_ALREADY_EXIST);
        }
        Address address = addressService.create(branch.getAddress());
        Branch newBranch = Branch.builder()
                .branchName(branch.getBranchName())
                .address(address)
                .phone(branch.getPhone())
                .workingTime(branch.getWorkingTime())
                .build();
        return branchRepository.save(newBranch);
    }

    @Override
    public Branch getById(Long branchId) {
        Optional<Branch> branchOpt = branchRepository.findById(branchId);
        Branch branch = branchOpt.orElseThrow(
                () -> new SubjectNotFoundException(ErrorMessage.BRANCH_ID_WAS_NOT_FOUND));
        return branch;
    }

    @Override
    public Branch getByName(String branchName) {
       Branch branch = branchRepository.findByBranchName(branchName);
        if (branch == null) {
            throw new SubjectNotFoundException(ErrorMessage.BRANCH_NAME_WAS_NOT_FOUND);
        }
        return branch;
    }

    @Override
    public List<Branch> getAllBranches() {
        List<Branch> branchList = branchRepository.findAll();
        return branchList.isEmpty() ? Collections.emptyList() : branchList;
    }
}
