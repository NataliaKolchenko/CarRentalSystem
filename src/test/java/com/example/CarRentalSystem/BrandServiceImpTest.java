package com.example.CarRentalSystem;

import com.example.CarRentalSystem.exception.BrandAlreadyExistsException;
import com.example.CarRentalSystem.exception.BrandNotFoundException;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.repository.interfaces.BrandRepositoryInterface;
import com.example.CarRentalSystem.service.BrandServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class BrandServiceImpTest {

    @InjectMocks
    private BrandServiceImp brandService;
    @Mock
    private BrandRepositoryInterface brandRepository;

    @Test
    public void testCreateVehicleBrand_NewBrand_Successfully() {
        String brandName = "Test";

        when(brandRepository.getVehicleBrandByName(brandName)).thenReturn(null);

        Brand newBrand = new Brand(brandName);
        when(brandRepository.createVehicleBrand(brandName)).thenReturn(newBrand);

        Brand result = brandService.createVehicleBrand(brandName);

        assertNotNull(result);
        assertEquals(brandName, result.getBrandName());
    }

    @Test
    public void testCreateVehicleBrand_ExistingBrand_ThrowsException() {
        String brandName = "ExistingBrand";
        Brand existingBrand = new Brand(brandName);

        when(brandRepository.getVehicleBrandByName(brandName)).thenReturn(existingBrand);

        BrandAlreadyExistsException exception = assertThrows(BrandAlreadyExistsException.class, () ->
                brandService.createVehicleBrand(brandName));

        assertEquals("BrandName has to be unique", exception.getMessage());
    }

    @Test
    public void testUpdateVehicleBrand_ExistingBrand_Successfully(){
        Long existingId = 1L;
        String newBrandName = "NewBrandName";

        Brand existingBrand = new Brand();
        existingBrand.setId(existingId);
        existingBrand.setBrandName("Brand");

        when(brandRepository.getVehicleBrandById(existingId)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.getVehicleBrandByName(newBrandName)).thenReturn(null);

        Brand updatedBrand = new Brand();
        updatedBrand.setId(existingId);
        updatedBrand.setBrandName(newBrandName);

        when(brandRepository.updateVehicleBrand(existingBrand)).thenReturn(updatedBrand);
        Brand result = brandService.updateVehicleBrand(existingId, newBrandName);

        assertEquals(result, updatedBrand);
        assertEquals(result.getBrandName(), newBrandName);
    }

    @Test
    public void testUpdateVehicleBrand_NotUniqueBrand_ThrowsException() {
        Long existingId = 1L;
        String newBrandName = "ExistingBrand";

        Brand existingBrand = new Brand();
        existingBrand.setId(existingId);
        existingBrand.setBrandName("ExistingBrand");

        when(brandRepository.getVehicleBrandById(existingId)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.getVehicleBrandByName(newBrandName)).thenReturn(existingBrand);

        Brand updatedBrand = new Brand();
        updatedBrand.setId(existingId);
        updatedBrand.setBrandName(newBrandName);

        BrandAlreadyExistsException exception = assertThrows(BrandAlreadyExistsException.class, () ->
                brandService.updateVehicleBrand(existingId, newBrandName));

        assertEquals("BrandName has to be unique", exception.getMessage());
    }

    @Test
    public void testDeleteVehicleBrandById_ExistingBrandId_Successfully(){
        Long existingId = 1L;
        when(brandRepository.existsById(existingId)).thenReturn(true);
        when(brandRepository.deleteVehicleBrandById(existingId)).thenReturn(true);
        boolean result = brandService.deleteVehicleBrandById(existingId);

        assertTrue(result);
    }

    @Test
    public void testDeleteVehicleBrandById_BrandIdNotFound_ThrowsException(){
        Long notExistingId = 2L;
        when(brandRepository.existsById(notExistingId)).thenReturn(false);
        BrandNotFoundException exception = assertThrows(BrandNotFoundException.class,
                () -> brandService.deleteVehicleBrandById(notExistingId));
        assertEquals("BrandId was not found", exception.getMessage());
    }

    @Test
    public void testGetVehicleBrandById_Successfully(){
        Long brandId = 1L;
        Brand expectedBrand = new Brand("expectedBrand");
        when(brandRepository.getVehicleBrandById(brandId)).thenReturn(Optional.of((expectedBrand)));

        Brand vehicleBrandById = brandService.getVehicleBrandById(brandId);
        assertNotNull(vehicleBrandById);
        assertEquals(expectedBrand, vehicleBrandById);
    }

    @Test
    public void testGetVehicleBrandById_NotExistBrandId_ThrowsException(){
        Long brandId = 1L;
        when(brandRepository.getVehicleBrandById(brandId)).thenReturn(Optional.empty());

        BrandNotFoundException exception = assertThrows(BrandNotFoundException.class,
                () -> brandService.getVehicleBrandById(brandId));

        assertEquals("BrandId was not found", exception.getMessage());
    }

    @Test
    public void testGetVehicleBrandByName_Successfully(){
        String brandName = "expectedBrand";
        Brand expectedBrand = new Brand("expectedBrand");
        when(brandRepository.getVehicleBrandByName(brandName)).thenReturn(expectedBrand);

        Brand vehicleBrandByName = brandService.getVehicleBrandByName(brandName);
        assertEquals(expectedBrand, vehicleBrandByName);
        assertNotNull(vehicleBrandByName);
    }

    @Test
    public void testGetVehicleBrandByName_NotExistBrandId_ThrowsException(){
        String brandName = "expectedBrand";
        when(brandRepository.getVehicleBrandByName(brandName)).thenReturn(null);

        BrandNotFoundException exception = assertThrows(BrandNotFoundException.class,
                () -> brandService.getVehicleBrandByName(brandName));

        assertEquals("BrandId was not found", exception.getMessage());
    }

    @Test
    public void testGetAllVehicleBrand_Successfully(){
        List<Brand> brandList = new ArrayList<>();
        Brand brand1 = new Brand("Test1");
        Brand brand2 = new Brand("Test2");
        brandList.add(brand1);
        brandList.add(brand2);
        when(brandRepository.getAllVehicleBrand()).thenReturn(brandList);

        List<Brand> actualBrandList = brandService.getAllVehicleBrand();
        assertFalse(actualBrandList.isEmpty());
        assertEquals(brandList, actualBrandList);
        assertEquals(brandList.size(), actualBrandList.size());
    }

    @Test
    public void testGetAllVehicleBrand_EmptyList(){
        when(brandRepository.getAllVehicleBrand()).thenReturn(Collections.emptyList());

        List<Brand> brandList = brandService.getAllVehicleBrand();

        assertEquals(Collections.emptyList(), brandList);
        assertTrue(brandList.isEmpty());
    }
}
