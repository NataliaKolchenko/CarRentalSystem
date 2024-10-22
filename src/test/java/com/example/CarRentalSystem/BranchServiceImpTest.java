package com.example.CarRentalSystem;

import com.example.CarRentalSystem.model.Address;
import com.example.CarRentalSystem.model.Branch;
import com.example.CarRentalSystem.repository.JpaBranchRepository;
import com.example.CarRentalSystem.service.AddressServiceImp;
import com.example.CarRentalSystem.service.BranchServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BranchServiceImpTest {
    @InjectMocks
    private BranchServiceImp branchService;
    @Mock
    private AddressServiceImp addressService;
    @Mock
    private JpaBranchRepository branchRepository;

    @Test
    public void testCreate_NewBranch_Successfully() {
        Address savedAddress = Address.builder()
                .country("country")
                .city("city")
                .street("street")
                .house(1)
                .apartment("apartment")
                .build();

        Branch branch = Branch.builder()
                .branchName("BranchName")
                .phone("123123123")
                .address(savedAddress)
                .workingTime("time")
                .build();

        when(branchRepository.findByBranchName(branch.getBranchName())).thenReturn(null);

        when(addressService.create(any(Address.class))).thenReturn(savedAddress);
        when(branchRepository.save(any(Branch.class))).thenReturn(branch);

        Branch resultBranch = branchService.create(branch);

        assertAll(
                () -> assertNotNull(resultBranch),
                () -> assertEquals(branch, resultBranch),
                () -> assertEquals(savedAddress, resultBranch.getAddress()),

                () -> verify(branchRepository).findByBranchName(branch.getBranchName()),
                () -> verify(addressService).create(savedAddress),
                () -> verify(branchRepository).save(any(Branch.class))
        );
    }
}
