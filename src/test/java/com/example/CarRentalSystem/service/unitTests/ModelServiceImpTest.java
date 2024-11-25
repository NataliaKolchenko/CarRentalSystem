package com.example.CarRentalSystem.service.unitTests;

import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.model.Model;
import com.example.CarRentalSystem.repository.JpaModelRepository;
import com.example.CarRentalSystem.service.BrandServiceImp;
import com.example.CarRentalSystem.service.ModelServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private JpaModelRepository modelRepository;

    @Test
    public void testCreateModel_NewModel_Successfully() {
        Long existBrandId = 1L;
        String existBrandName = "existBrandName";

        Brand brand = new Brand();
        brand.setId(existBrandId);
        brand.setBrandName(existBrandName);

        String modelName = "modelName";
        Model model = new Model(modelName, brand);

        when(modelRepository.findByModelName(modelName)).thenReturn(null);
        when(brandService.getById(existBrandId)).thenReturn(brand);
        when(modelRepository.save(any(Model.class))).thenReturn(model);

        Model resultModel = modelService.create(model);

        assertAll(
                () -> assertNotNull(resultModel),
                () -> assertEquals(modelName, resultModel.getModelName()),

                () -> verify(modelRepository).findByModelName(modelName),
                () -> verify(brandService).getById(existBrandId),
                () -> verify(modelRepository).save(any(Model.class))
        );

    }

    @Test
    public void testCreateModel_ModelNameNotUnique_ThrowsException() {
        Long existBrandId = 1L;
        String existModelName = "existModelName";

        Brand existBrand = new Brand();
        existBrand.setId(existBrandId);
        existBrand.setBrandName("BrandName");

        Model model = new Model(existModelName, existBrand);

        when(modelRepository.findByModelName(existModelName)).thenReturn(model);

        SubjectAlreadyExistsException exception = assertThrows(SubjectAlreadyExistsException.class, () ->
                modelService.create(model));

        assertAll(
                () -> assertEquals("ModelName has to be unique", exception.getMessage()),

                () -> verify(modelRepository).findByModelName(existModelName),
                () -> verifyNoMoreInteractions(modelRepository)
        );
    }

    @Test
    public void testUpdateModel_ExistingModel_Successfully() {
        Long existModelId = 1L;
        Brand existBrand = new Brand("existBrandName");

        Model existingModel = new Model();
        existingModel.setId(existModelId);
        existingModel.setModelName("ExistingModelName");
        existingModel.setBrand(existBrand);

        String newModelName = "NewModelName";
        existingModel.setModelName(newModelName);

        when(modelRepository.findByModelName(existingModel.getModelName())).thenReturn(null);
        when(modelRepository.findById(existModelId)).thenReturn(Optional.of(existingModel));
        when(modelRepository.save(any(Model.class))).thenReturn(existingModel);

        Model resultModel = modelService.update(existModelId, newModelName);

        assertAll(
                () -> assertEquals(resultModel, existingModel),
                () -> assertEquals(resultModel.getModelName(), newModelName),

                () -> verify(modelRepository).findByModelName(newModelName),
                () -> verify(modelRepository).save(existingModel)
        );

    }

    @Test
    public void testUpdateModel_NotUniqueModel_ThrowsException() {
        Long existModelId = 1L;
        Brand existBrand = new Brand("existBrandName");

        Model existingModel = new Model();
        existingModel.setId(existModelId);
        existingModel.setModelName("ExistingModelName");
        existingModel.setBrand(existBrand);

        String newModelName = "ExistingModelName";
        existingModel.setModelName(newModelName);

        when(modelRepository.findByModelName(existingModel.getModelName())).thenReturn(existingModel);
        when(modelRepository.findById(existModelId)).thenReturn(Optional.of(existingModel));

        SubjectAlreadyExistsException exception = assertThrows(SubjectAlreadyExistsException.class, () ->
                modelService.update(existModelId, newModelName));
        assertAll(
                () -> assertEquals("ModelName has to be unique", exception.getMessage()),

                () -> verify(modelRepository).findByModelName(newModelName),
                () -> verifyNoMoreInteractions(modelRepository)
        );
    }

    @Test
    public void testGetModelById_Successfully() {
        Long modelId = 1L;
        Model expectedModel = new Model();
        expectedModel.setId(modelId);
        expectedModel.setModelName("ModelName");
        expectedModel.setBrand(new Brand("BrandName"));

        when(modelRepository.findById(modelId)).thenReturn(Optional.of(expectedModel));

        Model modelById = modelService.getById(modelId);

        assertAll(
                () -> assertNotNull(modelById),
                () -> assertEquals(expectedModel, modelById),

                () -> verify(modelRepository).findById(modelId)
        );
    }

    @Test
    public void testGeModelById_NotExistModelId_ThrowsException() {
        Long modelId = 1L;

        when(modelRepository.findById(modelId)).thenReturn(Optional.empty());

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> modelService.getById(modelId));

        assertAll(
                () -> assertEquals("ModelId was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(modelRepository)
        );

    }

    @Test
    public void testGeModelByName_Successfully() {
        String modelName = "expectedModel";
        Model expectedModel = new Model(modelName, new Brand("Brand"));

        when(modelRepository.findByModelName(modelName)).thenReturn(expectedModel);

        Model modelByName = modelService.getByName(modelName);

        assertAll(
                () -> assertNotNull(modelByName),
                () -> assertEquals(expectedModel, modelByName),

                () -> verify(modelRepository).findByModelName(modelName)
        );
    }

    @Test
    public void testGetModelByName_NotExistModelId_ThrowsException() {
        String modelName = "expectedModel";
        when(modelRepository.findByModelName(modelName)).thenReturn(null);

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> modelService.getByName(modelName));

        assertAll(
                () -> assertEquals("ModelName was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(modelRepository)
        );
    }

    @Test
    public void testGetAllModels_Successfully() {
        List<Model> modelList = new ArrayList<>();
        Brand brand1 = new Brand("Test1");
        Model model1 = new Model("Model1", brand1);
        Model model2 = new Model("Model2", brand1);

        modelList.add(model1);
        modelList.add(model2);

        when(modelRepository.findAll()).thenReturn(modelList);

        List<Model> actualModelList = modelService.getAllModels();

        assertAll(
                () -> assertFalse(actualModelList.isEmpty()),
                () -> assertEquals(modelList, actualModelList),
                () -> assertEquals(modelList.size(), actualModelList.size()),

                () -> verify(modelRepository).findAll()
        );
    }

    @Test
    public void testGetAllModels_EmptyList() {
        when(modelRepository.findAll()).thenReturn(Collections.emptyList());
        List<Model> modelList = modelService.getAllModels();

        assertAll(
                () -> assertEquals(Collections.emptyList(), modelList),
                () -> assertTrue(modelList.isEmpty()),

                () -> verifyNoMoreInteractions(modelRepository)
        );
    }

    @Test
    public void testDeleteById_ModelIdIsExist_Successfully() {
        Long modelId = 1L;
        when(modelRepository.existsById(modelId)).thenReturn(true);

        modelService.deleteById(modelId);

        verify(modelRepository).deleteById(modelId);
    }

    @Test
    public void testDeleteById_ModelIdNotExist_ThrowsException() {
        Long modelId = 1L;
        when(modelRepository.existsById(modelId)).thenReturn(false);

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> modelService.deleteById(modelId));

        assertAll(
                () -> assertEquals("ModelId was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(modelRepository)
        );

    }

    @Test
    public void testExistsByBrandId_true() {
        Long brandId = 1L;
        Brand brand = new Brand("Brand");
        brand.setId(brandId);

        Model model = new Model("Model", brand);
        List<Model> modelList = List.of(model);

        when(modelRepository.findByBrandId(brandId)).thenReturn(modelList);
        boolean result = modelService.existsByBrandId(brandId);

        assertAll(
                () -> assertTrue(result),

                () -> verify(modelRepository).findByBrandId(brandId)
        );
    }

    @Test
    public void testExistsByBrandId_false() {
        Long brandId = 1L;

        List<Model> modelList = new ArrayList<>();

        when(modelRepository.findByBrandId(brandId)).thenReturn(modelList);
        boolean result = modelService.existsByBrandId(brandId);

        assertAll(
                () -> assertFalse(result),

                () -> verify(modelRepository).findByBrandId(brandId)
        );
    }
}
