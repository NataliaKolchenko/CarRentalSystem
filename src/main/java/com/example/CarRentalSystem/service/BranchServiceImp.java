package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.Address;
import com.example.CarRentalSystem.model.Branch;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.repository.JpaAddressRepository;
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
    private final AddressServiceImp addressService;

    @Autowired
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
