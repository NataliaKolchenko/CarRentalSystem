package com.example.CarRentalSystem.controller.intergationTests;

import com.example.CarRentalSystem.exception.error.ErrorCarRentalSystem;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.model.Model;
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
    public void testCreateNewBrand_EmptyBody_InvalidInput() throws Exception {
        Brand brand = new Brand();
        String jsonRequest = objectMapper.writeValueAsString(brand);

        MvcResult result = mockMvc.perform(post("/brandAndModel/createNewBrand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void testCreateNewBrand_SpaceBrandName_InvalidInput() throws Exception {
        Brand brand = new Brand(" ");
        String jsonRequest = objectMapper.writeValueAsString(brand);

        MvcResult result = mockMvc.perform(post("/brandAndModel/createNewBrand")
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
                        .contains("brandName may not be blank or null or has spaces"))
        );

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
                .andExpect(jsonPath("$.errorDescriptionList[0]")
                        .value(ErrorMessage.BRAND_NAME_IS_ALREADY_EXIST))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.BRAND_NAME_IS_ALREADY_EXIST))
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
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.BRAND_ID_WAS_NOT_FOUND))
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
        Long brandId = 101L;

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

    @Sql("/data/insert_data.sql")
    @Test
    public void testDeleteBrandById_CanNotDelete() throws Exception {
        Long brandId = 100L;

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
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains(ErrorMessage.CANNOT_DELETE_BRAND))
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
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.BRAND_ID_WAS_NOT_FOUND))
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
    public void testUpdateNewBrand_EmptyBody_InvalidInput() throws Exception {
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
                .andExpect(jsonPath("$.errorDescriptionList[0]")
                        .value(ErrorMessage.BRAND_NAME_IS_ALREADY_EXIST))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.BRAND_NAME_IS_ALREADY_EXIST))
        );
    }

    // -----------------------------------------------------------------

    @Sql("/data/insert_data.sql")
    @Test
    public void testCreateNewModel_Success() throws Exception {
        Brand existingBrand = new Brand();
        existingBrand.setId(100L);
        existingBrand.setBrandName("Ford");

        Model model = new Model("Focus", existingBrand);

        String jsonRequest = objectMapper.writeValueAsString(model);

        MvcResult result = mockMvc.perform(post("/brandAndModel/createNewModel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        Model modelAfterCreation = objectMapper.readValue(jsonResponse, Model.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertNotNull(modelAfterCreation.getId()),
                () -> assertEquals(model.getModelName(), modelAfterCreation.getModelName()),
                () -> assertEquals(model.getBrand().getId(), modelAfterCreation.getBrand().getId()),
                () -> assertEquals(model.getBrand().getBrandName(), modelAfterCreation.getBrand().getBrandName())
        );

    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testCreateNewModel_ModelNameIsAlreadyExist() throws Exception {
        Brand existingBrand = new Brand();
        existingBrand.setId(100L);
        existingBrand.setBrandName("Ford");

        Model model = new Model("Mustang", existingBrand);

        String jsonRequest = objectMapper.writeValueAsString(model);

        MvcResult result = mockMvc.perform(post("/brandAndModel/createNewModel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorDescriptionList").isArray())
                .andExpect(jsonPath("$.errorDescriptionList[0]")
                        .value(ErrorMessage.MODEL_NAME_IS_ALREADY_EXIST))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.MODEL_NAME_IS_ALREADY_EXIST))
        );

    }

    @Test
    public void testCreateNewModel_EmptyBody_InvalidInput() throws Exception {
        Model model = new Model();
        String jsonRequest = objectMapper.writeValueAsString(model);

        MvcResult result = mockMvc.perform(post("/brandAndModel/createNewModel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void testCreateNewModel_SpaceModelName_InvalidInput() throws Exception {
        Brand existingBrand = new Brand();
        existingBrand.setId(100L);
        existingBrand.setBrandName("Ford");

        Model model = new Model(" ", existingBrand);
        String jsonRequest = objectMapper.writeValueAsString(model);

        MvcResult result = mockMvc.perform(post("/brandAndModel/createNewModel")
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
                        .contains("modelName may not be blank or null or has spaces"))
        );

    }

    @Test
    public void testCreateNewModel_NullBrand_InvalidInput() throws Exception {
        Brand existingBrand = null;

        Model model = new Model("Model", existingBrand);
        String jsonRequest = objectMapper.writeValueAsString(model);

        MvcResult result = mockMvc.perform(post("/brandAndModel/createNewModel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("Brand may not be null"))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetModelById_Success() throws Exception {
        Long modelId = 100L;

        MvcResult result =
                mockMvc.perform(get("/brandAndModel/getModelById/{id}", modelId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Model modelAfterCreation = objectMapper.readValue(jsonResponse, Model.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(modelId, modelAfterCreation.getId()),
                () -> assertNotNull(modelAfterCreation)
        );

    }

    @Test
    public void testGetModelById_ModelIdWasNotFound() throws Exception {
        Long modelId = 1000L;

        MvcResult result =
                mockMvc.perform(get("/brandAndModel/getModelById/{id}", modelId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.MODEL_ID_WAS_NOT_FOUND))
        );
    }


    @Sql("/data/insert_data.sql")
    @Test
    public void testGetAllModels_Success() throws Exception {
        mockMvc.perform(get("/brandAndModel/getAllModels")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [{"id":100,
                                "modelName":"Mustang",
                                "brand":{
                                    "id":100,
                                    "brandName":"Ford"}},
                                {"id":101,
                                "modelName":"GT",
                                "brand":{
                                     "id":100,
                                    "brandName":"Ford"}}]
                                """))
                .andReturn();
    }

    @Sql("/data/delete_data.sql")
    @Test
    public void testGetAllModels_EmptyList() throws Exception {
        mockMvc.perform(get("/brandAndModel/getAllModels")
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
    public void testDeleteModelById_Success() throws Exception {
        Long modelId = 101L;

        MvcResult result =
                mockMvc.perform(delete("/brandAndModel/deleteModelById/{id}", modelId)
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
    public void testDeleteModelById_ModelIdWasNotFound() throws Exception {
        Long modelId = 1000L;

        MvcResult result =
                mockMvc.perform(delete("/brandAndModel/deleteModelById/{id}", modelId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.MODEL_ID_WAS_NOT_FOUND))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testUpdateModel_Success() throws Exception {
        Long modelId = 100L;
        Brand existingBrand = new Brand("Brand");
        existingBrand.setId(101L);

        Model modelRequest = new Model("NewModelName", existingBrand);

        String jsonRequest = objectMapper.writeValueAsString(modelRequest);

        MvcResult result = mockMvc.perform(put("/brandAndModel/updateModel/{id}", modelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Model modelResponse = objectMapper.readValue(jsonResponse, Model.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(modelResponse.getModelName(), modelRequest.getModelName()),
                () -> assertEquals(modelId, modelResponse.getId())
        );
    }

    @Test
    public void testUpdateModel_EmptyBody_InvalidInput() throws Exception {
        Long modelId = 100L;
        Model model = new Model();
        String jsonRequest = objectMapper.writeValueAsString(model);

        MvcResult result = mockMvc.perform(put("/brandAndModel/updateModel/{id}", modelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void testUpdateModel_NullBrand_InvalidInput() throws Exception {
        Long modelId = 100L;
        Brand brand = null;
        Model model = new Model("model", brand);
        String jsonRequest = objectMapper.writeValueAsString(model);

        MvcResult result = mockMvc.perform(put("/brandAndModel/updateModel/{id}", modelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testUpdateModel_ModelNameIsAlreadyExist() throws Exception {
        Long modelId = 100L;
        Brand existingBrand = new Brand("Ford");
        existingBrand.setId(100L);

        Model modelRequest = new Model("Mustang", existingBrand);

        String jsonRequest = objectMapper.writeValueAsString(modelRequest);

        MvcResult result = mockMvc.perform(put("/brandAndModel/updateModel/{id}", modelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorDescriptionList").isArray())
                .andExpect(jsonPath("$.errorDescriptionList[0]")
                        .value(ErrorMessage.MODEL_NAME_IS_ALREADY_EXIST))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.MODEL_NAME_IS_ALREADY_EXIST))
        );
    }

}

