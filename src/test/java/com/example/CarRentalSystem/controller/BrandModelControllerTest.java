package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.model.Model;
import com.example.CarRentalSystem.service.BrandServiceImp;
import com.example.CarRentalSystem.service.interfaces.ModelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@WebMvcTest(BrandModelController.class)
public class BrandModelControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BrandServiceImp brandService;
    @MockBean
    private ModelService modelService;
    private Brand brand;
    private Model model;

    @Test
    public void testCreateNewBrand_Success() throws Exception {
        Long brandId = 1l;
        brand = new Brand("NewBrand");
        brand.setId(brandId);

        when(brandService.createVehicleBrand(brand.getBrandName())).thenReturn(brand);
        when(brandService.getVehicleBrandByName(brand.getBrandName())).thenReturn(brand);

        mockMvc.perform(post("/brandAndModel/createNewBrand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "brandName": "NewBrand"
                                }
                                """)) // Тело запроса
                .andExpect(status().isOk()) // Ожидаемый статус 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(brandId.intValue())))
                .andExpect(jsonPath("$.brandName", is("NewBrand"))); // Проверка возвращаемого значения
    }

    @Test
    public void testCreateNewBrand_InvalidInput() throws Exception {
        mockMvc.perform(post("/brandAndModel/createNewBrand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "brandName": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetBrandById_Success() throws Exception {
        Long brandId = 1L;
        brand = new Brand();
        brand.setId(brandId);
        brand.setBrandName("Brand");

        when(brandService.getVehicleBrandById(brandId)).thenReturn(brand);

        mockMvc.perform(get("/brandAndModel/getBrandById/{id}", brandId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(brandId.intValue())))
                .andExpect(jsonPath("$.brandName", is("Brand")));
    }

    @Test
    public void testGetAllBrands() throws Exception {
        brand = new Brand("Brand");
        Brand brand2 = new Brand("Brand2");

        List<Brand> brandList = List.of(brand, brand2);

        when(brandService.getAllVehicleBrand()).thenReturn(brandList);

        mockMvc.perform(get("/brandAndModel/getAllBrands"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testDeleteBrandById() throws Exception {
        Long brandId = 1L;

        doNothing().when(brandService).deleteVehicleBrandById(brandId);

        mockMvc.perform(delete("/brandAndModel/deleteBrandById/{id}", brandId))
                .andExpect(status().isOk());

    }

    @Test
    public void testUpdateBrand() throws Exception {
        Long brandId = 1L;
        brand = new Brand();
        brand.setId(brandId);
        brand.setBrandName("Brand");

        String newBrandName = "NewBrandName";

        Brand updatedBrand = new Brand();
        updatedBrand.setId(brandId);
        updatedBrand.setBrandName(newBrandName);

        when(brandService.updateVehicleBrand(brandId, newBrandName)).thenReturn(updatedBrand);

        mockMvc.perform(put("/brandAndModel/updateBrand/{id}", brandId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "brandName": "NewBrandName"
                                }
                                """)) // Тело запроса

                .andExpect(status().isOk()) // Ожидаемый статус 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(brandId.intValue())))
                .andExpect(jsonPath("$.brandName", is("NewBrandName"))); // Проверка возвращаемого значения


    }

    // -----------------------------------------------------------------

    @Test
    public void testCreateNewModel_Success() throws Exception {
        Long brandId = 2L;
        String brandName = "BrandName";
        brand = new Brand();
        brand.setId(brandId);
        brand.setBrandName(brandName);

        Long modelId = 1l;
        String modelName = "ModelName";
        model = new Model(modelName, brand);
        model.setId(modelId);

        when(modelService.createModel(model)).thenReturn(model);
        when(modelService.getModelByName(model.getModelName())).thenReturn(model);

        mockMvc.perform(post("/brandAndModel/createNewModel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "modelName": "ModelName",
                            "brand": {
                                "id": 2,
                                "brandName": "BrandName"
                            }
                        }
                        """))
                .andExpect(status().isOk()) // Ожидаемый статус 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(modelId.intValue()))) // Проверка возвращаемого значения
                .andExpect(jsonPath("$.modelName", is(modelName)))
                .andExpect(jsonPath("$.brand.id", is(brandId.intValue())))
                .andExpect(jsonPath("$.brand.brandName", is(brandName)));
    }

    @Test
    public void testCreateNewModel_InvalidInput() throws Exception {
        mockMvc.perform(post("/brandAndModel/createNewModel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "modelName": "",
                            "brand": {
                                "id": 2,
                                "brandName": "BrandName"
                            }
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetModelById_Success() throws Exception {
        Long brandId = 1L;
        String brandName = "BrandName";
        brand = new Brand(brandName);
        brand.setId(brandId);

        Long modelId = 2L;
        String modelName = "ModelName";
        model = new Model(modelName, brand);
        model.setId(modelId);

        when(modelService.getModelById(modelId)).thenReturn(model);

        mockMvc.perform(get("/brandAndModel/getModelById/{id}", modelId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(modelId.intValue())))
                .andExpect(jsonPath("$.modelName", is(modelName)))
                .andExpect(jsonPath("$.brand.id", is(brandId.intValue())))
                .andExpect(jsonPath("$.brand.brandName", is(brandName)));
    }

    @Test
    public void testGetAllModels() throws Exception {
        Long brandId = 1L;
        String brandName = "BrandName";
        brand = new Brand(brandName);
        brand.setId(brandId);

        model = new Model("Model1", brand);
        Model model2 = new Model("Model2", brand);

        List<Model> modelList = List.of(model, model2);

        when(modelService.getAllModels()).thenReturn(modelList);

        mockMvc.perform(get("/brandAndModel/getAllModels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testDeleteModelById() throws Exception {
        Long modelId = 1L;

        doNothing().when(modelService).deleteModelById(modelId);

        mockMvc.perform(delete("/brandAndModel/deleteModelById/{id}", modelId))
                .andExpect(status().isOk());

    }

    @Test
    public void testUpdateModel() throws Exception {
        Long brandId = 1L;
        brand = new Brand();
        brand.setId(brandId);
        brand.setBrandName("Brand");

        Long modelId = 2L;
        model = new Model("ExistingModel", brand);
        model.setId(modelId);

        String newModelName = "NewModelName";

        Model updatedModel = new Model();
        updatedModel.setId(modelId);
        updatedModel.setModelName(newModelName);
        updatedModel.setBrand(brand);

        when(modelService.updateModel(modelId, newModelName)).thenReturn(updatedModel);

        mockMvc.perform(put("/brandAndModel/updateModel/{id}", modelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "modelName": "NewModelName",
                            "brand": {
                                "id": 1,
                                "brandName": "Brand"
                            }
                        }
                        """))
                .andExpect(status().isOk()) // Ожидаемый статус 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(modelId.intValue())))
                .andExpect(jsonPath("$.modelName", is(newModelName)))
                .andExpect(jsonPath("$.brand.id", is(brandId.intValue())))
                .andExpect(jsonPath("$.brand.brandName", is(brand.getBrandName())));

    }

}
