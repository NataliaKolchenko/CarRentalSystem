package com.example.CarRentalSystem.controller.intergationTests;

import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.enums.EngineType;
import com.example.CarRentalSystem.enums.TransmissionType;
import com.example.CarRentalSystem.exception.error.ErrorCarRentalSystem;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.*;
import com.example.CarRentalSystem.model.dto.VehicleRequestDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/data/drop_table.sql")
@Sql("/data/schema_test.sql")
@WithMockUser(username = "real_admin@real.admin", password = "REAL_ADMIN", roles = "ADMIN")
public class VehicleControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    @Sql("/data/insert_data.sql")
    @Test
    public void testCreateVehicle_Success() throws Exception {
        VehicleRequestDto vehicleRequestDto = new VehicleRequestDto(
                100L,
                100L,
                true,
                100L,
                100L,
                EngineType.PETROL,
                2007,
                100L,
                TransmissionType.MANUAL,
                190788,
                City.BERLIN,
                true,
                "ACUJ33",
                "3123FF");

        String  jsonRequest = objectMapper.writeValueAsString(vehicleRequestDto);

        MvcResult result = mockMvc.perform(post("/vehicle/createVehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        Vehicle vehicleResponse = objectMapper.readValue(jsonResponse, Vehicle.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertNotNull(vehicleResponse),
                () -> assertNotNull(vehicleResponse.getId())
        );
    }

    @Test
    public void testCreateVehicle_EmptyBody_InvalidInput() throws Exception {
        Vehicle vehicle = null;
        String jsonRequest = objectMapper.writeValueAsString(vehicle);

        MvcResult result = mockMvc.perform(post("/vehicle/createVehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testCreateVehicle_NullFields_InvalidInput() throws Exception {

        VehicleRequestDto vehicleRequestDto2 = new VehicleRequestDto(
                100L,
                100L,
                true,
                100L,
                100L,
                null,
                0,
                100L,
                null,
                -1,
                null,
                true,
                null,
                null);

        String jsonRequest = objectMapper.writeValueAsString(vehicleRequestDto2);


        MvcResult result = mockMvc.perform(post("/vehicle/createVehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("engineType may not be null")),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("year may not be less then 1950")),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("transmissionType may not be null")),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("mileage may not be negative")),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("city may not be blank")),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("vinCode may not be blank")),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("vehicleNumber may not be blank"))

        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testCreateNewVehicle_VehicleIsAlreadyExist() throws Exception {
        VehicleRequestDto vehicleRequestDto = new VehicleRequestDto(
                100L,
                100L,
                true,
                100L,
                100L,
                EngineType.PETROL,
                2007,
                100L,
                TransmissionType.MANUAL,
                190788,
                City.BERLIN,
                true,
                "AAA123",
                "SSS555");

        String jsonRequest = objectMapper.writeValueAsString(vehicleRequestDto);

        MvcResult result = mockMvc.perform(post("/vehicle/createVehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorDescriptionList").isArray())
                .andExpect(jsonPath("$.errorDescriptionList[0]")
                        .value(ErrorMessage.VEHICLE_IS_ALREADY_EXIST))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.VEHICLE_IS_ALREADY_EXIST))
        );

    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetVehicleById_Success() throws Exception {
        Long vehicleId = 103L;

        MvcResult result =
                mockMvc.perform(get("/vehicle/getVehicleById/{id}", vehicleId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Vehicle vehicleResponse = objectMapper.readValue(jsonResponse, Vehicle.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(vehicleId, vehicleResponse.getId()),
                () -> assertNotNull(vehicleResponse)
        );
    }

    @Test
    public void testGetVehicleById_VehicleIdWasNotFound() throws Exception {
        Long vehicleId = 1000L;

        MvcResult result =
                mockMvc.perform(get("/vehicle/getVehicleById/{id}", vehicleId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.VEHICLE_ID_WAS_NOT_FOUND))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetAllVehicles_Success() throws Exception {
        mockMvc.perform(get("/vehicle/getAllVehicles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [{"id":100,"type":{"id":100,"vehicleTypeName":"Auto"},"subType":{"id":100,"subTypeName":"Lux","type":{"id":100,"vehicleTypeName":"Auto"}},"active":true,"brand":{"id":100,"brandName":"Ford"},"model":{"id":100,"modelName":"Mustang","brand":{"id":100,"brandName":"Ford"}},"engineType":"PETROL","year":1999,"branch":{"id":100,"branchName":"Berlin Brunch","address":{"id":100,"zipCode":"14000","country":"Germany","region":"BERLIN","city":"BERLIN","district":"Main district","street":"Mainstraße","house":2,"apartment":"GMbH CarRentalSystem","additionalInfo":"2 OG","createDate":"2024-11-12T12:00:00"},"phone":"+496872211","workingTime":"MO - FR: 9:00 - 18:00"},"transmissionType":"MANUAL","mileage":230445,"city":"BERLIN","favorite":true,"vinCode":"AAA123","vehicleNumber":"SSS555","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":101,"type":{"id":100,"vehicleTypeName":"Auto"},"subType":{"id":100,"subTypeName":"Lux","type":{"id":100,"vehicleTypeName":"Auto"}},"active":true,"brand":{"id":100,"brandName":"Ford"},"model":{"id":100,"modelName":"Mustang","brand":{"id":100,"brandName":"Ford"}},"engineType":"PETROL","year":1998,"branch":{"id":100,"branchName":"Berlin Brunch","address":{"id":100,"zipCode":"14000","country":"Germany","region":"BERLIN","city":"BERLIN","district":"Main district","street":"Mainstraße","house":2,"apartment":"GMbH CarRentalSystem","additionalInfo":"2 OG","createDate":"2024-11-12T12:00:00"},"phone":"+496872211","workingTime":"MO - FR: 9:00 - 18:00"},"transmissionType":"MANUAL","mileage":230440,"city":"BERLIN","favorite":false,"vinCode":"BBB111","vehicleNumber":"UUU444","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":102,"type":{"id":100,"vehicleTypeName":"Auto"},"subType":{"id":100,"subTypeName":"Lux","type":{"id":100,"vehicleTypeName":"Auto"}},"active":true,"brand":{"id":100,"brandName":"Ford"},"model":{"id":100,"modelName":"Mustang","brand":{"id":100,"brandName":"Ford"}},"engineType":"PETROL","year":1998,"branch":{"id":100,"branchName":"Berlin Brunch","address":{"id":100,"zipCode":"14000","country":"Germany","region":"BERLIN","city":"BERLIN","district":"Main district","street":"Mainstraße","house":2,"apartment":"GMbH CarRentalSystem","additionalInfo":"2 OG","createDate":"2024-11-12T12:00:00"},"phone":"+496872211","workingTime":"MO - FR: 9:00 - 18:00"},"transmissionType":"MANUAL","mileage":230440,"city":"BERLIN","favorite":false,"vinCode":"YYY555","vehicleNumber":"HHH664","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":103,"type":{"id":100,"vehicleTypeName":"Auto"},"subType":{"id":100,"subTypeName":"Lux","type":{"id":100,"vehicleTypeName":"Auto"}},"active":true,"brand":{"id":100,"brandName":"Ford"},"model":{"id":100,"modelName":"Mustang","brand":{"id":100,"brandName":"Ford"}},"engineType":"PETROL","year":1998,"branch":{"id":100,"branchName":"Berlin Brunch","address":{"id":100,"zipCode":"14000","country":"Germany","region":"BERLIN","city":"BERLIN","district":"Main district","street":"Mainstraße","house":2,"apartment":"GMbH CarRentalSystem","additionalInfo":"2 OG","createDate":"2024-11-12T12:00:00"},"phone":"+496872211","workingTime":"MO - FR: 9:00 - 18:00"},"transmissionType":"MANUAL","mileage":230440,"city":"BERLIN","favorite":false,"vinCode":"TTT434","vehicleNumber":"EEE333","createDate":"2024-11-12T12:00:00","updateDate":null}]
                                """))
                .andReturn();
    }

    @Sql("/data/delete_data.sql")
    @Test
    public void testGetAllModels_EmptyList() throws Exception {
        mockMvc.perform(get("/vehicle/getAllVehicles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                ]
                                """))
                .andReturn();
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testUpdateModel_Success() throws Exception {
        Long vehicleExistId = 100L;
        VehicleRequestDto vehicleRequestDto = new VehicleRequestDto(
                100L,
                100L,
                true,
                100L,
                100L,
                EngineType.PETROL,
                2007,
                100L,
                TransmissionType.MANUAL,
                200000,
                City.BONN,
                true,
                "AAA123",
                "SSS555");


        String jsonRequest = objectMapper.writeValueAsString(vehicleRequestDto);

        MvcResult result = mockMvc.perform(put("/vehicle/updateVehicle/{id}", vehicleExistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Vehicle vehicleResponse = objectMapper.readValue(jsonResponse, Vehicle.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(vehicleRequestDto.getMileage(), vehicleResponse.getMileage()),
                () -> assertEquals(vehicleExistId, vehicleResponse.getId()),
                () -> assertEquals(vehicleRequestDto.getCity(), vehicleResponse.getCity())
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetFavoriteVehicles_Success() throws Exception {
        mockMvc.perform(get("/vehicle/getFavoriteVehicles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [{"id":100,
                                "type":{"id":100,"vehicleTypeName":"Auto"},
                                "subType":{"id":100,"subTypeName":"Lux","type":{"id":100,"vehicleTypeName":"Auto"}},
                                "active":true,
                                "brand":{"id":100,"brandName":"Ford"},
                                "model":{"id":100,"modelName":"Mustang","brand":{"id":100,"brandName":"Ford"}},
                                "engineType":"PETROL",
                                "year":1999,
                                "branch":{"id":100,"branchName":"Berlin Brunch","address":{"id":100,"zipCode":"14000","country":"Germany","region":"BERLIN","city":"BERLIN","district":"Main district","street":"Mainstraße","house":2,"apartment":"GMbH CarRentalSystem","additionalInfo":"2 OG","createDate":"2024-11-12T12:00:00"},"phone":"+496872211","workingTime":"MO - FR: 9:00 - 18:00"},
                                "transmissionType":"MANUAL",
                                "mileage":230445,
                                "city":"BERLIN",
                                "favorite":true,
                                "vinCode":"AAA123",
                                "vehicleNumber":"SSS555",
                                "createDate":"2024-11-12T12:00:00",
                                "updateDate":null}]
                                """))
                .andReturn();
    }

    @Sql("/data/delete_data.sql")
    @Test
    public void testGetFavoriteVehicles_EmptyList() throws Exception {
        mockMvc.perform(get("/vehicle/getFavoriteVehicles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                ]
                                """))
                .andReturn();
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testAddToFavorites_Success() throws Exception {
        Long vehicleId = 102L;

        String jsonRequest = objectMapper.writeValueAsString(vehicleId);

        MvcResult result =
                mockMvc.perform(put("/vehicle/addToFavorites")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isOk())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Boolean responseResult = objectMapper.readValue(jsonResponse, Boolean.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(Boolean.TRUE, responseResult)
        );

    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testRemoveToFavorites_Success() throws Exception {
        Long vehicleId = 101L;

        String jsonRequest = objectMapper.writeValueAsString(vehicleId);

        MvcResult result =
                mockMvc.perform(put("/vehicle/removeToFavorites")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isOk())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Boolean responseResult = objectMapper.readValue(jsonResponse, Boolean.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(Boolean.TRUE, responseResult)
        );

    }

}
