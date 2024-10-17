package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.SubType;
import com.example.CarRentalSystem.model.VehicleType;
import com.example.CarRentalSystem.service.VehicleTypeServiceImp;
import com.example.CarRentalSystem.service.interfaces.SubTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TypeSubtypeController.class)
public class TypeSubtypeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VehicleTypeServiceImp typeService;
    @MockBean
    private SubTypeService subTypeService;
    private VehicleType type;
    private SubType subType;
    @Test
    public void testCreateNewType_Success() throws Exception {
        Long typeId = 1l;
        type = new VehicleType("NewType");
        type.setId(typeId);

        when(typeService.create(type.getVehicleTypeName())).thenReturn(type);
        when(typeService.getByName(type.getVehicleTypeName())).thenReturn(type);

        mockMvc.perform(post("/typeAndSubtype/createNewType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vehicleTypeName\":\"NewType\"}")) // Тело запроса
                .andExpect(status().isOk()) // Ожидаемый статус 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(typeId.intValue())))
                .andExpect(jsonPath("$.vehicleTypeName", is("NewType"))); // Проверка возвращаемого значения
    }

    @Test
    public void testCreateNewType_InvalidInput() throws Exception {
        mockMvc.perform(post("/typeAndSubtype/createNewType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vehicleTypeName\":\" \"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetTypeById_Success() throws Exception {
        Long typeId = 1L;
        type = new VehicleType();
        type.setId(typeId);
        type.setVehicleTypeName("Type");

        when(typeService.getById(typeId)).thenReturn(type);

        mockMvc.perform(get("/typeAndSubtype/getTypeById/{id}", typeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(typeId.intValue())))
                .andExpect(jsonPath("$.vehicleTypeName", is("Type")));
    }

    @Test
    public void testGetAllTypes() throws Exception {
        type = new VehicleType("Type");
        VehicleType type2 = new VehicleType("Type2");

        List<VehicleType> typeListList = List.of(type, type2);

        when(typeService.getAll()).thenReturn(typeListList);

        mockMvc.perform(get("/typeAndSubtype/getAllTypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testDeleteTypeById() throws Exception {
        Long typeId = 1L;

        doNothing().when(typeService).deleteById(typeId);

        mockMvc.perform(delete("/typeAndSubtype/deleteTypeById/{id}", typeId))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateType() throws Exception {
        Long typeId = 1L;
        type = new VehicleType();
        type.setId(typeId);
        type.setVehicleTypeName("Type");

        String newTypeName = "NewTypeName";

        VehicleType updatedType = new VehicleType();
        updatedType.setId(typeId);
        updatedType.setVehicleTypeName(newTypeName);

        when(typeService.update(typeId, newTypeName)).thenReturn(updatedType);

        mockMvc.perform(put("/typeAndSubtype/updateType/{id}", typeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vehicleTypeName\":\"NewTypeName\"}")) // Тело запроса
                .andExpect(status().isOk()) // Ожидаемый статус 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(typeId.intValue())))
                .andExpect(jsonPath("$.vehicleTypeName", is("NewTypeName"))); // Проверка возвращаемого значения
    }

    // -----------------------------------------------------------------
    @Test
    public void testCreateNewSubType_Success() throws Exception {
        Long typeId = 2L;
        String typeName = "TypeName";
        type = new VehicleType();
        type.setId(typeId);
        type.setVehicleTypeName(typeName);

        Long subTypeId = 1l;
        String subTypeName = "SubTypeName";
        subType = new SubType(subTypeName, type);
        subType.setId(subTypeId);

        when(subTypeService.create(subType)).thenReturn(subType);
        when(subTypeService.getByName(subType.getSubTypeName())).thenReturn(subType);

        mockMvc.perform(post("/typeAndSubtype/createNewSubType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"subTypeName\": \"SubTypeName\",\n" +
                                "    \"type\": {\n" +
                                "        \"id\": 1,\n" +
                                "        \"vehicleTypeName\": \"TypeName\"\n" +
                                "    }\n" +
                                "}")) // Тело запроса
                .andExpect(status().isOk()) // Ожидаемый статус 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(subTypeId.intValue()))) // Проверка возвращаемого значения
                .andExpect(jsonPath("$.subTypeName", is(subTypeName)))
                .andExpect(jsonPath("$.type.id", is(typeId.intValue())))
                .andExpect(jsonPath("$.type.vehicleTypeName", is(typeName)));
    }

    @Test
    public void testCreateNewSubType_InvalidInput() throws Exception {
        mockMvc.perform(post("/typeAndSubtype/createNewSubType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"subTypeName\": \" \",\n" +
                                "    \"type\": {\n" +
                                "        \"id\": 1,\n" +
                                "        \"vehicleTypeName\": \"TypeName\"\n" +
                                "    }\n" +
                                "}")) // Тело запроса
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetSubTypeById_Success() throws Exception {
        Long typeId = 1L;
        String typeName = "TypeName";
        type = new VehicleType(typeName);
        type.setId(typeId);

        Long subTypeId = 2L;
        String subTypeName = "ModelName";
        subType = new SubType(subTypeName, type);
        subType.setId(subTypeId);

        when(subTypeService.getById(subTypeId)).thenReturn(subType);

        mockMvc.perform(get("/typeAndSubtype/getSubTypeById/{id}", subTypeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(subTypeId.intValue())))
                .andExpect(jsonPath("$.subTypeName", is(subTypeName)))
                .andExpect(jsonPath("$.type.id", is(typeId.intValue())))
                .andExpect(jsonPath("$.type.vehicleTypeName", is(typeName)));
    }
    @Test
    public void testGetAllSubTypes() throws Exception {
        Long typedId = 1L;
        String typeName = "BrandName";
        type = new VehicleType(typeName);
        type.setId(typedId);

        subType = new SubType("SubType1", type);
        SubType subType2 = new SubType("SubType2", type);

        List<SubType> subTypeList = List.of(subType, subType2);

        when(subTypeService.getAllSubTypes()).thenReturn(subTypeList);

        mockMvc.perform(get("/typeAndSubtype/getAllSubTypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testDeleteSubTypeById() throws Exception {
        Long subTypeId = 1L;

        doNothing().when(subTypeService).deleteById(subTypeId);

        mockMvc.perform(delete("/typeAndSubtype/deleteSubTypeById/{id}", subTypeId))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateSubType() throws Exception {
        Long typeId = 1L;
        type = new VehicleType();
        type.setId(typeId);
        type.setVehicleTypeName("Type");

        Long subTypeId = 2L;
        subType = new SubType("ExistingSubType", type);
        subType.setId(subTypeId);

        String newSubTypeName = "NewSubTypeName";

        SubType updatedSubType = new SubType();
        updatedSubType.setId(subTypeId);
        updatedSubType.setSubTypeName(newSubTypeName);
        updatedSubType.setType(type);

        when(subTypeService.update(subTypeId, newSubTypeName)).thenReturn(updatedSubType);

        mockMvc.perform(put("/typeAndSubtype/updateSubType/{id}", subTypeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"subTypeName\": \"NewSubTypeName\",\n" +
                                "    \"type\": {\n" +
                                "        \"id\": 1,\n" +
                                "        \"vehicleTypeName\": \"Type\"\n" +
                                "    }\n" +
                                "}")) // Тело запроса
                .andExpect(status().isOk()) // Ожидаемый статус 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(subTypeId.intValue())))
                .andExpect(jsonPath("$.subTypeName", is(newSubTypeName)))
                .andExpect(jsonPath("$.type.id", is(typeId.intValue())))
                .andExpect(jsonPath("$.type.vehicleTypeName", is(type.getVehicleTypeName())));

    }
}
