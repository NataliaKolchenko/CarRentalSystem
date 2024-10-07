package com.example.CarRentalSystem;

import com.example.CarRentalSystem.exception.BrandAlreadyExistsException;
import com.example.CarRentalSystem.exception.BrandNotFoundException;
import com.example.CarRentalSystem.model.Brand;
import com.example.CarRentalSystem.repository.interfaces.BrandRepositoryInterface;
import com.example.CarRentalSystem.service.BrandServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class BrandServiceImpTest {

    @InjectMocks
    private BrandServiceImp brandService;
    @Mock
    private BrandRepositoryInterface brandRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
    public void testGetVehicleBrandById_Successfully(){
        Long brandId = 1L;
        Optional<Brand> expectedBrand = Optional.of(new Brand("expectedBrand"));
        when(brandRepository.getVehicleBrandById(brandId)).thenReturn((expectedBrand));

        assertEquals(expectedBrand, brandService.getVehicleBrandById(brandId));
        assertNotNull(expectedBrand);
    }

    @Test
    public void testGetVehicleBrandById_NotExistBrandId_ThrowsException(){
        Long brandId = 1L;
        when(brandRepository.getVehicleBrandById(brandId)).thenReturn(null);

        BrandNotFoundException exception = assertThrows(BrandNotFoundException.class, () ->
                brandService.getVehicleBrandById(brandId));

        assertEquals("BrandId was not found", exception.getMessage());
    }

    @Test
    public void testGetVehicleBrandByName_Successfully(){
        String brandName = "expectedBrand";
        Brand expectedBrand = new Brand("expectedBrand");
        when(brandRepository.getVehicleBrandByName(brandName)).thenReturn(expectedBrand);

        assertEquals(expectedBrand, brandService.getVehicleBrandByName(brandName));
        assertNotNull(expectedBrand);
    }

    @Test
    public void testGetVehicleBrandByName_NotExistBrandId_ThrowsException(){
        String brandName = "expectedBrand";
        when(brandRepository.getVehicleBrandByName(brandName)).thenReturn(null);

        BrandNotFoundException exception = assertThrows(BrandNotFoundException.class, () ->
                brandService.getVehicleBrandByName(brandName));

        assertEquals("BrandId was not found", exception.getMessage());
    }
}
