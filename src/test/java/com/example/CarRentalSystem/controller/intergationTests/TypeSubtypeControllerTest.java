package com.example.CarRentalSystem.controller.intergationTests;

import com.example.CarRentalSystem.exception.error.ErrorCarRentalSystem;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.entity.SubType;
import com.example.CarRentalSystem.model.entity.VehicleType;
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
public class TypeSubtypeControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testCreateNewType_Success() throws Exception {
        VehicleType type = new VehicleType("Moto");
        String jsonRequest = objectMapper.writeValueAsString(type);

        MvcResult result = mockMvc.perform(post("/typeAndSubtype/createNewType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        VehicleType typeResponse = objectMapper.readValue(jsonResponse, VehicleType.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertNotNull(typeResponse.getVehicleTypeName()),
                () -> assertNotNull(typeResponse.getId()),
                () -> assertEquals(typeResponse.getVehicleTypeName(), typeResponse.getVehicleTypeName())
        );

    }

    @Test
    public void testCreateNewType_EmptyBody_InvalidInput() throws Exception {
        VehicleType type = new VehicleType();
        String jsonRequest = objectMapper.writeValueAsString(type);

        MvcResult result = mockMvc.perform(post("/typeAndSubtype/createNewType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void testCreateNewType_SpaceTypeName_InvalidInput() throws Exception {
        VehicleType type = new VehicleType(" ");
        String jsonRequest = objectMapper.writeValueAsString(type);

        MvcResult result = mockMvc.perform(post("/typeAndSubtype/createNewType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains("vehicleType may not be blank or null or has spaces"))
        );

    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testCreateNewType_TypeNameIsAlreadyExist() throws Exception {
        VehicleType type = new VehicleType("Auto");
        String jsonRequest = objectMapper.writeValueAsString(type);

        MvcResult result = mockMvc.perform(post("/typeAndSubtype/createNewType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorDescriptionList").isArray())
                .andExpect(jsonPath("$.errorDescriptionList[0]")
                        .value(ErrorMessage.TYPE_NAME_IS_ALREADY_EXIST))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.TYPE_NAME_IS_ALREADY_EXIST))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetTypeById_Success() throws Exception {
        Long typeId = 100L;

        MvcResult result =
                mockMvc.perform(get("/typeAndSubtype/getTypeById/{id}", typeId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        VehicleType typeResponse = objectMapper.readValue(jsonResponse, VehicleType.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(typeId, typeResponse.getId()),
                () -> assertNotNull(typeResponse.getVehicleTypeName())
        );

    }

    @Test
    public void testGetTypeById_TypeIdWasNotFound() throws Exception {
        Long typeId = 1000L;

        MvcResult result =
                mockMvc.perform(get("/typeAndSubtype/getTypeById/{id}", typeId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.TYPE_ID_WAS_NOT_FOUND))
        );
    }


    @Sql("/data/insert_data.sql")
    @Test
    public void testGetAllTypes_Success() throws Exception {
        mockMvc.perform(get("/typeAndSubtype/getAllTypes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                {"id":100,"vehicleTypeName":"Auto"},
                                {"id":101,"vehicleTypeName":"Bus"}
                                ]
                                """))
                .andReturn();
    }

    @Sql("/data/delete_data.sql")
    @Test
    public void testGetAllTypes_EmptyList() throws Exception {
        mockMvc.perform(get("/typeAndSubtype/getAllTypes")
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
    public void testDeleteTypeById_Success() throws Exception {
        Long typeId = 101L;

        MvcResult result =
                mockMvc.perform(delete("/typeAndSubtype/deleteTypeById/{id}", typeId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals("true", result.getResponse().getContentAsString())
        );

    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testDeleteTypeById_CanNotDelete() throws Exception {
        Long typeId = 100L;

        MvcResult result =
                mockMvc.perform(delete("/typeAndSubtype/deleteTypeById/{id}", typeId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains(ErrorMessage.CANNOT_DELETE_TYPE))
        );

    }

    @Sql("/data/delete_data.sql")
    @Test
    public void testDeleteTypeById_TypeIdWasNotFound() throws Exception {
        Long typeId = 1000L;

        MvcResult result =
                mockMvc.perform(delete("/typeAndSubtype/deleteTypeById/{id}", typeId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.TYPE_ID_WAS_NOT_FOUND))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testUpdateType_Success() throws Exception {
        Long typeId = 100L;
        VehicleType newTypeName = new VehicleType("Ship");

        String jsonRequest = objectMapper.writeValueAsString(newTypeName);

        MvcResult result = mockMvc.perform(put("/typeAndSubtype/updateType/{id}", typeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        VehicleType typeResponse = objectMapper.readValue(jsonResponse, VehicleType.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(newTypeName.getVehicleTypeName(), typeResponse.getVehicleTypeName()),
                () -> assertEquals(typeId, typeResponse.getId())
        );
    }

    @Test
    public void testUpdateNewType_EmptyBody_InvalidInput() throws Exception {
        Long typeId = 100L;
        VehicleType type = new VehicleType();
        String jsonRequest = objectMapper.writeValueAsString(type);

        MvcResult result = mockMvc.perform(put("/typeAndSubtype/updateType/{id}", typeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testUpdateType_TypeNameIsAlreadyExist() throws Exception {
        Long typeId = 100L;
        VehicleType type = new VehicleType("Bus");

        String jsonRequest = objectMapper.writeValueAsString(type);

        MvcResult result = mockMvc.perform(put("/typeAndSubtype/updateType/{id}", typeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorDescriptionList").isArray())
                .andExpect(jsonPath("$.errorDescriptionList[0]")
                        .value(ErrorMessage.TYPE_NAME_IS_ALREADY_EXIST))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.TYPE_NAME_IS_ALREADY_EXIST))
        );
    }

    // -----------------------------------------------------------------

    @Sql("/data/insert_data.sql")
    @Test
    public void testCreateNewSubType_Success() throws Exception {
        VehicleType type = new VehicleType();
        type.setId(100L);
        type.setVehicleTypeName("Auto");

        SubType subType = new SubType("Economy", type);

        String jsonRequest = objectMapper.writeValueAsString(subType);

        MvcResult result = mockMvc.perform(post("/typeAndSubtype/createNewSubType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        SubType subTypeResponse = objectMapper.readValue(jsonResponse, SubType.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertNotNull(subTypeResponse.getId()),
                () -> assertEquals(subType.getSubTypeName(), subTypeResponse.getSubTypeName()),
                () -> assertEquals(subType.getType().getId(), subTypeResponse.getType().getId()),
                () -> assertEquals(subType.getType().getVehicleTypeName(),
                        subTypeResponse.getType().getVehicleTypeName())
        );

    }

    @Test
    public void testCreateNewSubType_EmptyBody_InvalidInput() throws Exception {
        SubType subType = new SubType();
        String jsonRequest = objectMapper.writeValueAsString(subType);

        MvcResult result = mockMvc.perform(post("/typeAndSubtype/createNewSubType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void testCreateNewSubType_SpaceSubTypeName_InvalidInput() throws Exception {
        VehicleType type = new VehicleType();
        type.setId(100L);
        type.setVehicleTypeName("auto");

        SubType subType = new SubType(" ", type);

        String jsonRequest = objectMapper.writeValueAsString(subType);

        MvcResult result = mockMvc.perform(post("/typeAndSubtype/createNewSubType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains("subTypeName may not be blank or null or has spaces"))
        );
    }

    @Test
    public void testCreateNewSubType_NullType_InvalidInput() throws Exception {
        VehicleType type = null;

        SubType subType = new SubType("SubType", type);
        String jsonRequest = objectMapper.writeValueAsString(subType);

        MvcResult result = mockMvc.perform(post("/typeAndSubtype/createNewSubType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("VehicleType may not be null"))
        );
    }
    @Sql("/data/insert_data.sql")
    @Test
    public void testGetSubTypeById_Success() throws Exception {
        Long subTypeId = 100L;

        MvcResult result =
                mockMvc.perform(get("/typeAndSubtype/getSubTypeById/{id}", subTypeId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        SubType subTypeResponse = objectMapper.readValue(jsonResponse, SubType.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(subTypeId, subTypeResponse.getId()),
                () -> assertNotNull(subTypeResponse)
        );

    }

    @Test
    public void testGetSubTypeById_SubTypeIdWasNotFound() throws Exception {
        Long subTypeId = 1000L;

        MvcResult result =
                mockMvc.perform(get("/typeAndSubtype/getSubTypeById/{id}", subTypeId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.SUB_TYPE_ID_WAS_NOT_FOUND))
        );
    }


    @Sql("/data/insert_data.sql")
    @Test
    public void testGetAllSubTypes_Success() throws Exception {
        mockMvc.perform(get("/typeAndSubtype/getAllSubTypes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [{"id":100,
                                "subTypeName":"Lux",
                                "type":{
                                    "id":100,
                                    "vehicleTypeName":"Auto"}},
                                {"id":101,
                                "subTypeName":"Normal",
                                "type":{
                                     "id":100,
                                    "vehicleTypeName":"Auto"}}]
                                """))
                .andReturn();
    }

    @Sql("/data/delete_data.sql")
    @Test
    public void testGetAllSubTypes_EmptyList() throws Exception {
        mockMvc.perform(get("/typeAndSubtype/getAllSubTypes")
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
    public void testDeleteSubTypeById_Success() throws Exception {
        Long subTypeId = 101L;

        MvcResult result =
                mockMvc.perform(delete("/typeAndSubtype/deleteSubTypeById/{id}", subTypeId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals("true", result.getResponse().getContentAsString())
        );

    }

    @Sql("/data/delete_data.sql")
    @Test
    public void testDeleteSubTypeById_SubTypeIdWasNotFound() throws Exception {
        Long subTypeId = 1000L;

        MvcResult result =
                mockMvc.perform(delete("/typeAndSubtype/deleteSubTypeById/{id}", subTypeId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.SUB_TYPE_ID_WAS_NOT_FOUND))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testUpdateSubType_Success() throws Exception {
        Long subTypeId = 100L;
        VehicleType type = new VehicleType("Type");
        type.setId(101L);

        SubType subTypeRequest = new SubType("NewSubTypeName", type);

        String jsonRequest = objectMapper.writeValueAsString(subTypeRequest);

        MvcResult result = mockMvc.perform(put("/typeAndSubtype/updateSubType/{id}", subTypeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        SubType subTypeResponse = objectMapper.readValue(jsonResponse, SubType.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(subTypeRequest.getSubTypeName(), subTypeResponse.getSubTypeName()),
                () -> assertEquals(subTypeId, subTypeResponse.getId())
        );
    }

    @Test
    public void testUpdateSubType_EmptyBody_InvalidInput() throws Exception {
        Long subTypeId = 100L;
        VehicleType type = new VehicleType();
        String jsonRequest = objectMapper.writeValueAsString(type);

        MvcResult result = mockMvc.perform(put("/typeAndSubtype/updateSubType/{id}", subTypeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void testUpdateSubType_NullType_InvalidInput() throws Exception {
        Long subTypeId = 100L;
        VehicleType type = null;
        SubType subType = new SubType("subtype", type);

        String jsonRequest = objectMapper.writeValueAsString(subType);

        MvcResult result = mockMvc.perform(put("/typeAndSubtype/updateSubType/{id}", subTypeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("VehicleType may not be null"))
        );

    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testUpdateSubType_SubTypeNameIsAlreadyExist() throws Exception {
        Long subTypeId = 100L;
        VehicleType type = new VehicleType("Auto");
        type.setId(100L);

        SubType subType = new SubType("Lux", type);

        String jsonRequest = objectMapper.writeValueAsString(subType);

        MvcResult result = mockMvc.perform(put("/typeAndSubtype/updateSubType/{id}", subTypeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorDescriptionList").isArray())
                .andExpect(jsonPath("$.errorDescriptionList[0]")
                        .value(ErrorMessage.SUB_TYPE_NAME_IS_ALREADY_EXIST))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.SUB_TYPE_NAME_IS_ALREADY_EXIST))
        );
    }

}

