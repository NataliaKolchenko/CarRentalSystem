package com.example.CarRentalSystem;

import com.example.CarRentalSystem.controller.BrandModelController;
import com.example.CarRentalSystem.exception.BrandAlreadyExistsException;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.service.BrandServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@WebMvcTest(BrandModelController.class)
public class BrandModelControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BrandServiceImp brandService;

    private Brand brand;
    @BeforeEach
    public void setUp(){
        brand = new Brand("NewBrand");
    }

    @Test
    public void testCreateNewBrand_Success() throws Exception {
        // Настройка мока для успешного создания бренда
        when(brandService.createVehicleBrand(brand.getBrandName())).thenReturn(brand);
        when(brandService.getVehicleVehicleBrandByName(brand.getBrandName())).thenReturn(brand);

        // Выполнение POST-запроса
        mockMvc.perform(post("/brandAndModel/createNewBrand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brandName\":\"NewBrand\"}")) // Тело запроса
                .andExpect(status().isOk()) // Ожидаемый статус 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brandName", is("NewBrand"))); // Проверка возвращаемого значения
    }

    @Test
    public void testCreateNewBrand_Conflict() throws Exception {
        //настройка мока, когда бренд уже существует
        doThrow(new BrandAlreadyExistsException("BrandName has to be unique"))
                .when(brandService).createVehicleBrand(brand.getBrandName());

        mockMvc.perform(post("/brandAndModel/createNewBrand")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"brandName\":\"NewBrand\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("BrandName has to be unique"));


    }
}
