package com.example.CarRentalSystem;

import com.example.CarRentalSystem.exception.BrandAlreadyExistsException;
import com.example.CarRentalSystem.exception.ModelAlreadyExistsException;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.model.Model;
import com.example.CarRentalSystem.repository.brand.BrandRepository;
import com.example.CarRentalSystem.repository.model.ModelRepository;
import com.example.CarRentalSystem.service.BrandServiceImp;
import com.example.CarRentalSystem.service.ModelServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModelServiceImpTest {

    @InjectMocks
    private ModelServiceImp modelService;
    @Mock
    private BrandServiceImp brandService;
    @Mock
    private ModelRepository modelRepository;

    @Test
    public void testCreateModel_NewModel_Successfully(){
        Long existBrandId = 1L;
        String existBrandName = "existBrandName";

        Brand brand = new Brand();
        brand.setId(existBrandId);
        brand.setBrandName(existBrandName);

        String modelName = "modelName";
        Model model = new Model(modelName, brand);

        when(modelRepository.getModelByName(modelName)).thenReturn(null);
        when(brandService.getVehicleBrandById(existBrandId)).thenReturn(brand);
        when(modelRepository.createModel(any(Model.class))).thenReturn(model);

        Model resultModel = modelService.createModel(model);

        assertAll(
                () -> assertNotNull(resultModel),
                () -> assertEquals(modelName, resultModel.getModelName()),

                () -> verify(modelRepository).getModelByName(modelName),
                () -> verify(brandService).getVehicleBrandById(existBrandId),
                () -> verify(modelRepository).createModel(any(Model.class))
        );

    }

    @Test
    public void testCreateModel_ModelNameNotUnique_ThrowsException(){
        Long existBrandId = 1L;
        String existModelName = "existModelName";

        Brand existBrand = new Brand();
        existBrand.setId(existBrandId);
        existBrand.setBrandName("BrandName");

        Model model = new Model(existModelName, existBrand);

        when(modelRepository.getModelByName(existModelName)).thenReturn(model);

        ModelAlreadyExistsException exception = assertThrows(ModelAlreadyExistsException.class, () ->
                modelService.createModel(model));

        assertAll(
                () -> assertEquals("ModelName has to be unique", exception.getMessage()),

                () -> verify(modelRepository).getModelByName(existModelName),
                () -> verifyNoMoreInteractions(modelRepository)
        );
    }
}
