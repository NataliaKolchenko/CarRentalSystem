package com.example.CarRentalSystem.controller.intergationTests;

import com.example.CarRentalSystem.exception.error.ErrorCarRentalSystem;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.Brand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/data/drop_table.sql")
@Sql("/data/schema_test.sql")
@WithMockUser(username = "real_admin@real.admin", password = "REAL_ADMIN", roles = "ADMIN")
public class BrandModelControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testCreateNewBrand_Success() throws Exception {
        Brand brand = new Brand("createBrand");
        String jsonRequest = objectMapper.writeValueAsString(brand);

        MvcResult result = mockMvc.perform(post("/brandAndModel/createNewBrand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        Brand brandAfterCreation = objectMapper.readValue(jsonResponse, Brand.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertNotNull(brandAfterCreation.getBrandName()),
                () -> assertNotNull(brandAfterCreation.getId()),
                () -> assertEquals(brand.getBrandName(), brandAfterCreation.getBrandName())
        );

    }

    @Test
    public void testCreateNewBrand_InvalidInput() throws Exception {
        Brand brand = new Brand();
        String jsonRequest = objectMapper.writeValueAsString(brand);

        MvcResult result = mockMvc.perform(post("/brandAndModel/createNewBrand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testCreateNewBrand_BrandNameIsAlreadyExist() throws Exception {
        Brand brand = new Brand("Ford");
        String jsonRequest = objectMapper.writeValueAsString(brand);

        MvcResult result = mockMvc.perform(post("/brandAndModel/createNewBrand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorDescriptionList").isArray())
                .andExpect(jsonPath("$.errorDescriptionList[0]").value(ErrorMessage.BRAND_NAME_IS_ALREADY_EXIST))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains(ErrorMessage.BRAND_NAME_IS_ALREADY_EXIST))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetBrandById_Success() throws Exception {
        Long brandId = 100L;

        MvcResult result =
                mockMvc.perform(get("/brandAndModel/getBrandById/{id}", brandId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        Brand brandAfterFind = objectMapper.readValue(jsonResponse, Brand.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(brandId, brandAfterFind.getId()),
                () -> assertNotNull(brandAfterFind.getBrandName())
        );

    }

    @Test
    public void testGetBrandById_BrandIdWasNotFound() throws Exception {
        Long brandId = 1000L;

        MvcResult result =
                mockMvc.perform(get("/brandAndModel/getBrandById/{id}", brandId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains(ErrorMessage.BRAND_ID_WAS_NOT_FOUND))
        );
    }


    @Sql("/data/insert_data.sql")
    @Test
    public void testGetAllBrands_Success() throws Exception {
        mockMvc.perform(get("/brandAndModel/getAllBrands")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                {"id":100,"brandName":"Ford"},
                                {"id":101,"brandName":"Audi"}
                                ]
                                """))
                .andReturn();
    }

    @Sql("/data/delete_data.sql")
    @Test
    public void testGetAllBrands_EmptyList() throws Exception {
        mockMvc.perform(get("/brandAndModel/getAllBrands")
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
    public void testDeleteBrandById_Success() throws Exception {
        Long brandId = 100L;

        MvcResult result =
                mockMvc.perform(delete("/brandAndModel/deleteBrandById/{id}", brandId)
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
    public void testDeleteBrandById_BrandIdWasNotFound() throws Exception {
        Long brandId = 1000L;

        MvcResult result =
                mockMvc.perform(delete("/brandAndModel/deleteBrandById/{id}", brandId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains(ErrorMessage.BRAND_ID_WAS_NOT_FOUND))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testUpdateBrand_Success() throws Exception {
        Long brandId = 100L;

        Brand brandWithNewName = new Brand("NewBrand");
        String jsonRequest = objectMapper.writeValueAsString(brandWithNewName);

        MvcResult result = mockMvc.perform(put("/brandAndModel/updateBrand/{id}", brandId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Brand brandAfterUpdating = objectMapper.readValue(jsonResponse, Brand.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(brandWithNewName.getBrandName(), brandAfterUpdating.getBrandName()),
                () -> assertEquals(brandId, brandAfterUpdating.getId())
        );
    }

    @Test
    public void testUpdateNewBrand_InvalidInput() throws Exception {
        Long brandId = 100L;
        Brand brand = new Brand();
        String jsonRequest = objectMapper.writeValueAsString(brand);

        MvcResult result = mockMvc.perform(put("/brandAndModel/updateBrand/{id}", brandId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testUpdateBrand_BrandNameIsAlreadyExist() throws Exception {
        Long brandId = 100L;

        Brand brandWithNewName = new Brand("Ford");
        String jsonRequest = objectMapper.writeValueAsString(brandWithNewName);

        MvcResult result = mockMvc.perform(put("/brandAndModel/updateBrand/{id}", brandId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorDescriptionList").isArray())
                .andExpect(jsonPath("$.errorDescriptionList[0]").value(ErrorMessage.BRAND_NAME_IS_ALREADY_EXIST))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains(ErrorMessage.BRAND_NAME_IS_ALREADY_EXIST))
        );
    }

    // -----------------------------------------------------------------

//    @Test
//    public void testCreateNewModel_Success() throws Exception {
//        Long brandId = 2L;
//        String brandName = "BrandName";
//        brand = new Brand();
//        brand.setId(brandId);
//        brand.setBrandName(brandName);
//
//        Long modelId = 1l;
//        String modelName = "ModelName";
//        model = new Model(modelName, brand);
//        model.setId(modelId);
//
//        when(modelService.create(model)).thenReturn(model);
//        when(modelService.getByName(model.getModelName())).thenReturn(model);
//
//        mockMvc.perform(post("/brandAndModel/createNewModel")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("""
//                        {
//                            "modelName": "ModelName",
//                            "brand": {
//                                "id": 2,
//                                "brandName": "BrandName"
//                            }
//                        }
//                        """))
//                .andExpect(status().isOk()) // Ожидаемый статус 200
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id", is(modelId.intValue()))) // Проверка возвращаемого значения
//                .andExpect(jsonPath("$.modelName", is(modelName)))
//                .andExpect(jsonPath("$.brand.id", is(brandId.intValue())))
//                .andExpect(jsonPath("$.brand.brandName", is(brandName)));
//    }
//
//    @Test
//    public void testCreateNewModel_InvalidInput() throws Exception {
//        mockMvc.perform(post("/brandAndModel/createNewModel")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("""
//                        {
//                            "modelName": "",
//                            "brand": {
//                                "id": 2,
//                                "brandName": "BrandName"
//                            }
//                        }
//                        """))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void testGetModelById_Success() throws Exception {
//        Long brandId = 1L;
//        String brandName = "BrandName";
//        brand = new Brand(brandName);
//        brand.setId(brandId);
//
//        Long modelId = 2L;
//        String modelName = "ModelName";
//        model = new Model(modelName, brand);
//        model.setId(modelId);
//
//        when(modelService.getById(modelId)).thenReturn(model);
//
//        mockMvc.perform(get("/brandAndModel/getModelById/{id}", modelId))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id", is(modelId.intValue())))
//                .andExpect(jsonPath("$.modelName", is(modelName)))
//                .andExpect(jsonPath("$.brand.id", is(brandId.intValue())))
//                .andExpect(jsonPath("$.brand.brandName", is(brandName)));
//    }
//
//    @Test
//    public void testGetAllModels() throws Exception {
//        Long brandId = 1L;
//        String brandName = "BrandName";
//        brand = new Brand(brandName);
//        brand.setId(brandId);
//
//        model = new Model("Model1", brand);
//        Model model2 = new Model("Model2", brand);
//
//        List<Model> modelList = List.of(model, model2);
//
//        when(modelService.getAllModels()).thenReturn(modelList);
//
//        mockMvc.perform(get("/brandAndModel/getAllModels"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.length()").value(2));
//    }
//
//    @Test
//    public void testDeleteModelById() throws Exception {
//        Long modelId = 1L;
//
//        doNothing().when(modelService).deleteById(modelId);
//
//        mockMvc.perform(delete("/brandAndModel/deleteModelById/{id}", modelId))
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    public void testUpdateModel() throws Exception {
//        Long brandId = 1L;
//        brand = new Brand();
//        brand.setId(brandId);
//        brand.setBrandName("Brand");
//
//        Long modelId = 2L;
//        model = new Model("ExistingModel", brand);
//        model.setId(modelId);
//
//        String newModelName = "NewModelName";
//
//        Model updatedModel = new Model();
//        updatedModel.setId(modelId);
//        updatedModel.setModelName(newModelName);
//        updatedModel.setBrand(brand);
//
//        when(modelService.update(modelId, newModelName)).thenReturn(updatedModel);
//
//        mockMvc.perform(put("/brandAndModel/updateModel/{id}", modelId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("""
//                        {
//                            "modelName": "NewModelName",
//                            "brand": {
//                                "id": 1,
//                                "brandName": "Brand"
//                            }
//                        }
//                        """))
//                .andExpect(status().isOk()) // Ожидаемый статус 200
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id", is(modelId.intValue())))
//                .andExpect(jsonPath("$.modelName", is(newModelName)))
//                .andExpect(jsonPath("$.brand.id", is(brandId.intValue())))
//                .andExpect(jsonPath("$.brand.brandName", is(brand.getBrandName())));
//
//    }

}

