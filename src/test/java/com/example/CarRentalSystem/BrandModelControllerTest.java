package com.example.CarRentalSystem;

import com.example.CarRentalSystem.controller.BrandModelController;
import com.example.CarRentalSystem.model.Brand;
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

    @Test
    public void testCreateNewBrand_Success() throws Exception {
        Long brandId = 1l;
        brand = new Brand("NewBrand");
        brand.setId(brandId);

        when(brandService.createVehicleBrand(brand.getBrandName())).thenReturn(brand);
        when(brandService.getVehicleBrandByName(brand.getBrandName())).thenReturn(brand);

        mockMvc.perform(post("/brandAndModel/createNewBrand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brandName\":\"NewBrand\"}")) // Тело запроса
                .andExpect(status().isOk()) // Ожидаемый статус 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is(brandId.intValue())))
                .andExpect(jsonPath("$.brandName", is("NewBrand"))); // Проверка возвращаемого значения
    }

    @Test
    public void testCreateNewBrand_InvalidInput() throws Exception {
        mockMvc.perform(post("/brandAndModel/createNewBrand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brandName\":\" \"}"))
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
                .andExpect(jsonPath("$.id",is(brandId.intValue())))
                .andExpect(jsonPath("$.brandName",is("Brand")));
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
                        .content("{\"brandName\":\"NewBrandName\"}")) // Тело запроса
                .andExpect(status().isOk()) // Ожидаемый статус 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is(brandId.intValue())))
                .andExpect(jsonPath("$.brandName", is("NewBrandName"))); // Проверка возвращаемого значения


    }

}
