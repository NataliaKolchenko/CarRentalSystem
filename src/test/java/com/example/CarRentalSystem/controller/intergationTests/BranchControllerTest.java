package com.example.CarRentalSystem.controller.intergationTests;

import com.example.CarRentalSystem.model.enums.City;
import com.example.CarRentalSystem.exception.error.ErrorCarRentalSystem;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.entity.Address;
import com.example.CarRentalSystem.model.entity.Branch;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/data/drop_table.sql")
@Sql("/data/schema_test.sql")
@WithMockUser(username = "director@director.director", password = "DIRECTOR", roles = "DIRECTOR")
public class BranchControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateNewBranch_Success() throws Exception {
        Address address = Address.builder()
                .id(100L)
                .country("Germany")
                .city(City.BERLIN)
                .street("Mainstraße")
                .house(23)
                .apartment("GMbH CerRentalSystem")
                .build();


        Branch branch = Branch.builder()
                .branchName("Berlin Branch")
                .address(address)
                .phone("+49238768")
                .workingTime("9 - 18")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(branch);

        MvcResult result = mockMvc.perform(post("/branch/createNewBranch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        Branch branchResponse = objectMapper.readValue(jsonResponse, Branch.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(branch.getBranchName(), branchResponse.getBranchName()),
                () -> assertNotNull(branchResponse)
        );
    }

    @Test
    public void testCreateNewBranch_EmptyBody_InvalidInput() throws Exception {
        Branch branch = new Branch();
        String jsonRequest = objectMapper.writeValueAsString(branch);

        MvcResult result = mockMvc.perform(post("/branch/createNewBranch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testCreateNewBranch_SpacesFields_InvalidInput() throws Exception {
        Address address = Address.builder()
                .id(100L)
                .country("Germany")
                .city(City.BERLIN)
                .street("Mainstraße")
                .house(23)
                .apartment("GMbH CerRentalSystem")
                .build();

        Branch branch = Branch.builder()
                .branchName(" ")
                .address(address)
                .phone(" ")
                .workingTime(" ")
                .build();
        String jsonRequest = objectMapper.writeValueAsString(branch);

        MvcResult result = mockMvc.perform(post("/branch/createNewBranch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("BranchName may not be blank")),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("Phone may not be blank")),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("WorkingTime may not be blank"))


        );
    }

    @Test
    public void testCreateNewBranch_NullAddress_InvalidInput() throws Exception {
        Address address = null;

        Branch branch = Branch.builder()
                .branchName("Test")
                .address(address)
                .phone("Test")
                .workingTime("Test")
                .build();
        String jsonRequest = objectMapper.writeValueAsString(branch);

        MvcResult result = mockMvc.perform(post("/branch/createNewBranch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("Address may not be null"))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetBranchId_Success() throws Exception {
        Long branchId = 100L;

        MvcResult result =
                mockMvc.perform(get("/branch/getBranchById/{id}", branchId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        Branch branchResponse = objectMapper.readValue(jsonResponse, Branch.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(branchId, branchResponse.getId()),
                () -> assertNotNull(branchResponse.getBranchName())
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetBranchId_BranchIdWasNotFound() throws Exception {
        Long branchId = 1000L;

        MvcResult result =
                mockMvc.perform(get("/branch/getBranchById/{id}", branchId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.BRANCH_ID_WAS_NOT_FOUND))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetAllBranches_Success() throws Exception {
        mockMvc.perform(get("/branch/getAllBranches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                               [{"id":100,
                               "branchName":"Berlin Brunch",
                               "address":{"id":100,"zipCode":"14000","country":"Germany","region":"BERLIN","city":"BERLIN","district":"Main district","street":"Mainstraße","house":2,"apartment":"GMbH CarRentalSystem","additionalInfo":"2 OG","createDate":"2024-11-12T12:00:00"},
                               "phone":"+496872211",
                               "workingTime":"MO - FR: 9:00 - 18:00"},
                               
                               {"id":101,
                               "branchName":"Berlin Brunch",
                               "address":{"id":101,"zipCode":"14000","country":"Germany","region":"BERLIN","city":"BERLIN","district":"Main district","street":"Mainstraße","house":3,"apartment":"GMbH CarRentalSystem","additionalInfo":"2 OG","createDate":"2024-11-12T12:00:00"},
                               "phone":"+496872211",
                               "workingTime":"MO - FR: 9:00 - 18:00"}]
                                """))
                .andReturn();
    }

    @Sql("/data/delete_data.sql")
    @Test
    public void testGetAllBranches_EmptyList() throws Exception {
        mockMvc.perform(get("/branch/getAllBranches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                ]
                                """))
                .andReturn();
    }
}
