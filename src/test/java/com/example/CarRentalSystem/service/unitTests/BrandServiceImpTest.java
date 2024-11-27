package com.example.CarRentalSystem.service.unitTests;

import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotBeDeletedException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.repository.JpaBrandRepository;
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
public class BrandServiceImpTest {

    @InjectMocks
    private BrandServiceImp brandService;
    @Mock
    private JpaBrandRepository brandRepository;
    @Mock
    private ModelServiceImp modelService;

    @Test
    public void testCreateVehicleBrand_NewBrand_Successfully() {
        Brand brand = new Brand("Test");

        when(brandRepository.findByBrandName(brand.getBrandName())).thenReturn(null);

        when(brandRepository.save(any(Brand.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Brand result = brandService.create(brand);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(brand.getBrandName(), result.getBrandName()),

                () -> verify(brandRepository).findByBrandName(brand.getBrandName()),
                () -> verify(brandRepository).save(any(Brand.class))
        );
    }

    @Test
    public void testCreateVehicleBrand_ExistingBrand_ThrowsException() {
        Brand existingBrand = new Brand("ExistingBrand");

        when(brandRepository.findByBrandName(existingBrand.getBrandName())).thenReturn(existingBrand);

        SubjectAlreadyExistsException exception = assertThrows(SubjectAlreadyExistsException.class, () ->
                brandService.create(existingBrand));
        assertAll(
                () -> assertEquals("BrandName has to be unique", exception.getMessage()),

                () -> verify(brandRepository).findByBrandName(existingBrand.getBrandName()),
                () -> verifyNoMoreInteractions(brandRepository)
        );
    }

    @Test
    public void testUpdateVehicleBrand_ExistingBrand_Successfully() {
        Long existingId = 1L;
        Brand newBrandName = new Brand("NewBrandName");

        Brand existingBrand = new Brand();
        existingBrand.setId(existingId);
        existingBrand.setBrandName("Brand");

        when(brandRepository.findById(existingId)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.findByBrandName(newBrandName.getBrandName())).thenReturn(null);

        Brand updatedBrand = new Brand();
        updatedBrand.setId(existingId);
        updatedBrand.setBrandName(newBrandName.getBrandName());

        when(brandRepository.save(existingBrand)).thenReturn(updatedBrand);
        Brand result = brandService.update(existingId, newBrandName.getBrandName());

        assertAll(
                () -> assertEquals(result, updatedBrand),
                () -> assertEquals(result.getBrandName(), newBrandName.getBrandName()),

                () -> verify(brandRepository).findByBrandName(newBrandName.getBrandName()),
                () -> verify(brandRepository).save(existingBrand)
        );
    }

    @Test
    public void testUpdateVehicleBrand_NotUniqueBrand_ThrowsException() {
        Long existingId = 1L;
        String newBrandName = "ExistingBrand";

        Brand existingBrand = new Brand();
        existingBrand.setId(existingId);
        existingBrand.setBrandName("ExistingBrand");

        when(brandRepository.findById(existingId)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.findByBrandName(newBrandName)).thenReturn(existingBrand);

        Brand updatedBrand = new Brand();
        updatedBrand.setId(existingId);
        updatedBrand.setBrandName(newBrandName);

        SubjectAlreadyExistsException exception = assertThrows(SubjectAlreadyExistsException.class, () ->
                brandService.update(existingId, newBrandName));
        assertAll(
                () -> assertEquals("BrandName has to be unique", exception.getMessage()),

                () -> verify(brandRepository).findByBrandName(newBrandName),
                () -> verifyNoMoreInteractions(brandRepository)
        );
    }

    @Test
    public void testGetVehicleBrandById_Successfully() {
        Long brandId = 1L;
        Brand expectedBrand = new Brand("expectedBrand");
        when(brandRepository.findById(brandId)).thenReturn(Optional.of((expectedBrand)));

        Brand vehicleBrandById = brandService.getById(brandId);

        assertAll(
                () -> assertNotNull(vehicleBrandById),
                () -> assertEquals(expectedBrand, vehicleBrandById),

                () -> verify(brandRepository).findById(brandId)
        );


    }

    @Test
    public void testGetVehicleBrandById_NotExistBrandId_ThrowsException() {
        Long brandId = 1L;
        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> brandService.getById(brandId));

        assertAll(
                () -> assertEquals("BrandId was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(brandRepository)
        );

    }

    @Test
    public void testGetVehicleBrandByName_Successfully() {
        String brandName = "expectedBrand";
        Brand expectedBrand = new Brand("expectedBrand");
        when(brandRepository.findByBrandName(brandName)).thenReturn(expectedBrand);

        Brand vehicleBrandByName = brandService.getByName(brandName);

        assertAll(
                () -> assertNotNull(vehicleBrandByName),
                () -> assertEquals(expectedBrand, vehicleBrandByName),

                () -> verify(brandRepository).findByBrandName(brandName)
        );


    }

    @Test
    public void testGetVehicleBrandByName_NotExistBrandId_ThrowsException() {
        String brandName = "expectedBrand";
        when(brandRepository.findByBrandName(brandName)).thenReturn(null);

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> brandService.getByName(brandName));

        assertAll(
                () -> assertEquals("BrandName was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(brandRepository)
        );
    }

    @Test
    public void testGetAllVehicleBrand_Successfully() {
        List<Brand> brandList = new ArrayList<>();
        Brand brand1 = new Brand("Test1");
        Brand brand2 = new Brand("Test2");
        brandList.add(brand1);
        brandList.add(brand2);
        when(brandRepository.findAll()).thenReturn(brandList);

        List<Brand> actualBrandList = brandService.getAllBrands();

        assertAll(
                () -> assertFalse(actualBrandList.isEmpty()),
                () -> assertEquals(brandList, actualBrandList),
                () -> assertEquals(brandList.size(), actualBrandList.size()),

                () -> verify(brandRepository).findAll()
        );


    }

    @Test
    public void testGetAllVehicleBrand_EmptyList() {
        when(brandRepository.findAll()).thenReturn(Collections.emptyList());

        List<Brand> brandList = brandService.getAllBrands();

        assertAll(
                () -> assertEquals(Collections.emptyList(), brandList),
                () -> assertTrue(brandList.isEmpty()),

                () -> verifyNoMoreInteractions(brandRepository)
        );
    }

    @Test
    public void testDeleteById_BrandExistsAndNoModels_Successfully() {
        Long brandId = 1L;
        when(brandRepository.existsById(brandId)).thenReturn(true);
        when(modelService.existsByBrandId(brandId)).thenReturn(false);

        brandService.deleteById(brandId);

        verify(brandRepository).deleteById(brandId);
    }

    @Test
    public void testDeleteById_BrandIdNotExist_ThrowsException(){
        Long brandId = 1L;
        when(brandRepository.existsById(brandId)).thenReturn(false);

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> brandService.deleteById(brandId));

        assertAll(
                () -> assertEquals("BrandId was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(brandRepository)
        );
    }

    @Test
    public void testDeleteById_BrandHasModels_ThrowsException(){
        Long brandId = 1L;
        when(brandRepository.existsById(brandId)).thenReturn(true);
        when(modelService.existsByBrandId(brandId)).thenReturn(true);

        SubjectNotBeDeletedException exception = assertThrows(SubjectNotBeDeletedException.class,
                () -> brandService.deleteById(brandId));

        assertAll(
                () -> assertEquals("Can't delete brand with associated models", exception.getMessage()),

                () -> verifyNoMoreInteractions(brandRepository)
        );

    }
}
