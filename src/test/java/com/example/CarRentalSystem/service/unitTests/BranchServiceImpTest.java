package com.example.CarRentalSystem.service.unitTests;

import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
                .city(City.BERLIN)
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

    @Test
    public void testCreate_ExistingBranch_ThrowsException(){
        String branchName = "Branch";
        Branch branch = Branch.builder()
                .branchName(branchName)
                .build();

        when(branchRepository.findByBranchName(branchName)).thenReturn(branch);

        SubjectAlreadyExistsException exception = assertThrows(SubjectAlreadyExistsException.class,
                () -> branchService.create(branch));

        assertAll(
                () -> assertEquals("BranchName has to be unique", exception.getMessage()),

                () -> verify(branchRepository).findByBranchName(branchName),
                () -> verifyNoMoreInteractions(branchRepository)
        );
    }

    @Test
    public void testGetById_Successfully(){
        Long id = 1L;
        Branch expectedBranch = Branch.builder()
                .build();

        when(branchRepository.findById(id)).thenReturn(Optional.ofNullable(expectedBranch));

        Branch resultBranch = branchService.getById(id);

        assertAll(
                () -> assertNotNull(resultBranch),
                () -> assertEquals(expectedBranch, resultBranch),

                () -> verify(branchRepository).findById(id)
        );
    }

    @Test
    public void testGetById_NotExistBranchId_ThrowsException(){
        Long id = 1L;
        when(branchRepository.findById(id)).thenReturn(Optional.empty());

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> branchService.getById(id));

        assertAll(
                () -> assertEquals("BranchId was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(branchRepository)
        );
    }

    @Test
    public void testGetByName_Successfully(){
        String branchName = "BranchName";
        Branch expectedBranch = Branch.builder()
                .branchName(branchName)
                .build();

        when(branchRepository.findByBranchName(branchName)).thenReturn(expectedBranch);

        Branch resultBranch = branchService.getByName(branchName);

        assertAll(
                () -> assertNotNull(resultBranch),
                () -> assertEquals(expectedBranch, resultBranch),

                () -> verify(branchRepository).findByBranchName(branchName)
        );
    }

    @Test
    public void testGetByName_NotExistBranchId_ThrowsException(){
        String branchName = "BranchName";
        when(branchRepository.findByBranchName(branchName)).thenReturn(null);

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> branchService.getByName(branchName));

        assertAll(
                () -> assertEquals("BranchName was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(branchRepository)
        );
    }

    @Test
    public void testGetAllBranches_Successfully(){
        List<Branch> branchList = new ArrayList<>();
        Branch branch1 = Branch.builder().build();
        Branch branch2 = Branch.builder().build();
        branchList.add(branch1);
        branchList.add(branch2);
        when(branchRepository.findAll()).thenReturn(branchList);

        List<Branch> resultList = branchService.getAllBranches();

        assertAll(
                () -> assertFalse(resultList.isEmpty()),
                () -> assertEquals(branchList, resultList),
                () -> assertEquals(branchList.size(), resultList.size()),

                () -> verify(branchRepository).findAll()
        );

    }

    @Test
    public void testGetAllBranches_EmptyList() {
        when(branchRepository.findAll()).thenReturn(Collections.emptyList());

        List<Branch> branchList = branchService.getAllBranches();

        assertAll(
                () -> assertEquals(Collections.emptyList(), branchList),
                () -> assertTrue(branchList.isEmpty()),

                () -> verifyNoMoreInteractions(branchRepository)
        );
    }

}
