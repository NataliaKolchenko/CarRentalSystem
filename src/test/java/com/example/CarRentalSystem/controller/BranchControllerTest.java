package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.model.Address;
import com.example.CarRentalSystem.model.Branch;
import com.example.CarRentalSystem.service.BranchServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(BranchController.class)
public class BranchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BranchServiceImp branchService;
    private Branch branch;

    @Test
    public void testCreateNewBranch_Success() throws Exception {
        Long addressId = 2L;
        Address address = Address.builder()
                .country("Country")
                .city(City.BERLIN)
                .street("street")
                .house(1)
                .apartment("apartment")
                .build();
        address.setId(addressId);

        Long branchId = 1L;
        branch = Branch.builder()
                .branchName("branch name")
                .phone("988-22-33")
                .workingTime("9 - 18")
                .address(address)
                .build();
        branch.setId(branchId);

        when(branchService.create(branch)).thenReturn(branch);
        when(branchService.getByName(branch.getBranchName())).thenReturn(branch);

        mockMvc.perform(post("/branch/createNewBranch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "branchName": "branch name",
                                    "address": {
                                        "country": "Country",
                                        "city": "city",
                                        "street": "street.",
                                        "house": 1,
                                        "apartment": "apartment"
                                    },
                                    "phone": "988-22-33",
                                    "workingTime": "9 - 18"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(branchId.intValue())))
                .andExpect(jsonPath("$.branchName", is(branch.getBranchName())))
                .andExpect(jsonPath("$.address.id", is(addressId.intValue())))
                .andExpect(jsonPath("$.address.zipCode", is(branch.getAddress().getZipCode())))
                .andExpect(jsonPath("$.address.country", is(branch.getAddress().getCountry())))
                .andExpect(jsonPath("$.address.region", is(branch.getAddress().getRegion())))
                .andExpect(jsonPath("$.address.city", is(branch.getAddress().getCity())))
                .andExpect(jsonPath("$.address.district", is(branch.getAddress().getDistrict())))
                .andExpect(jsonPath("$.address.street", is(branch.getAddress().getStreet())))
                .andExpect(jsonPath("$.address.house", is(branch.getAddress().getHouse())))
                .andExpect(jsonPath("$.address.apartment", is(branch.getAddress().getApartment())))
                .andExpect(jsonPath("$.address.additionalInfo", is(branch.getAddress().getAdditionalInfo())))
                .andExpect(jsonPath("$.phone", is(branch.getPhone())))
                .andExpect(jsonPath("$.workingTime", is(branch.getWorkingTime())));

    }

    @Test
    public void testCreateNewBranch_InvalidInputMainObject() throws Exception{
        mockMvc.perform(post("/branch/createNewBranch")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                    "branchName": "",
                                    "address": {
                                        "country": "Country",
                                        "city": "city",
                                        "street": "street.",
                                        "house": 1,
                                        "apartment": "apartment"
                                    },
                                    "phone": "",
                                    "workingTime": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateNewBranch_InvalidInputNestedObject() throws Exception{
        mockMvc.perform(post("/branch/createNewBranch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "branchName": "branch name",
                                    "address": {
                                        "country": "",
                                        "city": "",
                                        "street": "",
                                        "house": ,
                                        "apartment": ""
                                    },
                                    "phone": "988-22-33",
                                    "workingTime": "9 - 18"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testCreateNewBranch_InvalidInputWholeRequest() throws Exception{
        mockMvc.perform(post("/branch/createNewBranch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "branchName": "",
                                    "address": {
                                        "country": "",
                                        "city": "",
                                        "street": "",
                                        "house": ,
                                        "apartment": ""
                                    },
                                    "phone": "",
                                    "workingTime": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetBranchById_Success() throws Exception{
        Long addressId = 2L;
        Address address = Address.builder()
                .country("Country")
                .city(City.BERLIN)
                .street("street")
                .house(1)
                .apartment("apartment")
                .build();
        address.setId(addressId);

        Long branchId = 1L;
        branch = Branch.builder()
                .branchName("branch name")
                .phone("988-22-33")
                .workingTime("9 - 18")
                .address(address)
                .build();
        branch.setId(branchId);

        when(branchService.getById(branchId)).thenReturn(branch);

        mockMvc.perform(get("/branch/getBranchById/{id}", branchId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(branchId.intValue())))
                .andExpect(jsonPath("$.branchName", is(branch.getBranchName())))
                .andExpect(jsonPath("$.address.id", is(addressId.intValue())))
                .andExpect(jsonPath("$.address.zipCode", is(branch.getAddress().getZipCode())))
                .andExpect(jsonPath("$.address.country", is(branch.getAddress().getCountry())))
                .andExpect(jsonPath("$.address.region", is(branch.getAddress().getRegion())))
                .andExpect(jsonPath("$.address.city", is(branch.getAddress().getCity())))
                .andExpect(jsonPath("$.address.district", is(branch.getAddress().getDistrict())))
                .andExpect(jsonPath("$.address.street", is(branch.getAddress().getStreet())))
                .andExpect(jsonPath("$.address.house", is(branch.getAddress().getHouse())))
                .andExpect(jsonPath("$.address.apartment", is(branch.getAddress().getApartment())))
                .andExpect(jsonPath("$.address.additionalInfo", is(branch.getAddress().getAdditionalInfo())))
                .andExpect(jsonPath("$.phone", is(branch.getPhone())))
                .andExpect(jsonPath("$.workingTime", is(branch.getWorkingTime())));
    }

    @Test
    public void testGetAllModels() throws Exception{
        Long addressId = 2L;
        Address address = Address.builder()
                .country("Country")
                .city(City.BERLIN)
                .street("street")
                .house(1)
                .apartment("apartment")
                .build();
        address.setId(addressId);

        branch = Branch.builder()
                .branchName("branch 1")
                .phone("111")
                .workingTime("9 - 18")
                .address(address)
                .build();

        Branch branch2 = Branch.builder()
                .branchName("branch 2")
                .phone("222")
                .workingTime("9 - 18")
                .address(address)
                .build();

        List<Branch> branchList = List.of(branch, branch2);

        when(branchService.getAllBranches()).thenReturn(branchList);

        mockMvc.perform(get("/branch/getAllBranches"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }
}
