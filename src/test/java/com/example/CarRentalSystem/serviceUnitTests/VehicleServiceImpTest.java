package com.example.CarRentalSystem.serviceUnitTests;

import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.enums.EngineType;
import com.example.CarRentalSystem.enums.TransmissionType;
import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.model.*;
import com.example.CarRentalSystem.model.dto.VehicleRequestDto;
import com.example.CarRentalSystem.repository.JpaVehicleRepository;
import com.example.CarRentalSystem.service.VehicleServiceImp;
import com.example.CarRentalSystem.service.VehicleTypeServiceImp;
import com.example.CarRentalSystem.service.interfaces.BranchService;
import com.example.CarRentalSystem.service.interfaces.BrandService;
import com.example.CarRentalSystem.service.interfaces.ModelService;
import com.example.CarRentalSystem.service.interfaces.SubTypeService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceImpTest {
    @InjectMocks
    private VehicleServiceImp vehicleService;

    @Mock
    private JpaVehicleRepository vehicleRepository;

    @Mock
    private VehicleTypeServiceImp typeService;

    @Mock
    private SubTypeService subTypeService;

    @Mock
    private BrandService brandService;

    @Mock
    private ModelService modelService;

    @Mock
    private BranchService branchService;

    @Test
    public void testCreate_NewVehicle_Successfully() {
        VehicleRequestDto dto = new VehicleRequestDto(1L, 2L, true, 3L, 4L,
                EngineType.DIESEL, 2021, 5L, TransmissionType.MANUAL, 15000, City.BERLIN,
                true, "12345", "12345");
        when(vehicleRepository.findByVinCodeAndVehicleNumber(dto.getVinCode(), dto.getVehicleNumber()))
                .thenReturn(Collections.emptyList());

        Vehicle vehicle = new Vehicle();
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        Vehicle createdVehicle = vehicleService.create(dto);

        assertAll(
                () -> assertNotNull(createdVehicle),
                () -> assertEquals(createdVehicle, vehicle),

                () -> verify(vehicleRepository).findByVinCodeAndVehicleNumber(dto.getVinCode(), dto.getVehicleNumber()),
                () -> verify(vehicleRepository).save(any(Vehicle.class))
        );

    }

    @Test
    public void testCreate_ExistingVehicle_ThrowsException() {
        VehicleRequestDto dto = new VehicleRequestDto(1L, 2L, true, 3L, 4L,
                EngineType.DIESEL, 2021, 5L, TransmissionType.MANUAL, 15000, City.BERLIN,
                true, "12345", "12345");
        Vehicle vehicle = new Vehicle();
        vehicle.setVinCode(dto.getVinCode());
        vehicle.setVehicleNumber(dto.getVehicleNumber());

        List<Vehicle> existingVehicles = List.of(vehicle);

        when(vehicleRepository.findByVinCodeAndVehicleNumber(dto.getVinCode(), dto.getVehicleNumber()))
                .thenReturn(existingVehicles);

        SubjectAlreadyExistsException exception = assertThrows(SubjectAlreadyExistsException.class,
                () -> vehicleService.create(dto));

        assertAll(
                () -> assertEquals("Vehicle is already exist", exception.getMessage()),

                () -> verifyNoMoreInteractions(vehicleRepository)
        );

    }

    @Test
    public void testGetById_Successfully() {
        Long id = 1L;
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);

        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.getById(id);

        assertAll(
                () -> assertNotNull(vehicle),
                () -> assertEquals(result, vehicle),

                () -> verify(vehicleRepository).findById(id)
        );
    }

    @Test
    public void testGetById_NotExistSubTypeId_ThrowsException() {
        Long id = 1L;

        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> vehicleService.getById(id));

        assertAll(
                () -> assertEquals("VehicleId was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(vehicleRepository)
        );
    }

    @Test
    public void testGetAllVehicles_Successfully() {
        List<Vehicle> vehicleList = new ArrayList<>();
        Vehicle vehicle1 = new Vehicle();
        Vehicle vehicle2 = new Vehicle();
        vehicleList.add(vehicle1);
        vehicleList.add(vehicle2);

        when(vehicleRepository.findAll()).thenReturn(vehicleList);

        List<Vehicle> actualVehiclesList = vehicleService.getAllVehicles();

        assertAll(
                () -> assertFalse(actualVehiclesList.isEmpty()),
                () -> assertEquals(vehicleList, actualVehiclesList),
                () -> assertEquals(vehicleList.size(), actualVehiclesList.size()),

                () -> verify(vehicleRepository).findAll()
        );
    }

    @Test
    public void testGetAllVehicles_EmptyList() {
        when(vehicleRepository.findAll()).thenReturn(Collections.emptyList());
        List<Vehicle> vehicleList = vehicleService.getAllVehicles();

        assertAll(
                () -> assertEquals(Collections.emptyList(), vehicleList),
                () -> assertTrue(vehicleList.isEmpty()),

                () -> verifyNoMoreInteractions(vehicleRepository)
        );
    }

    @Test
    public void testUpdateVehicle_ExistingId_Successfully() {
        Long existingId = 1L;
        Vehicle existingVehicle = new Vehicle();
        existingVehicle.setId(existingId);

        VehicleRequestDto requestDto = new VehicleRequestDto(1L, 2L, true, 3L, 4L,
                EngineType.DIESEL, 2021, 5L, TransmissionType.MANUAL, 15000, City.BERLIN, true,
                "12345", "12345");

        when(vehicleRepository.findById(existingId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(existingVehicle);

        Vehicle result = vehicleService.update(existingId, requestDto);

        assertAll(
                () -> assertEquals(result, existingVehicle),

                () -> verify(vehicleRepository).save(existingVehicle)
        );
    }

    @Test
    public void testGetFavoriteVehicles_Successfully() {
        List<Vehicle> vehicleList = new ArrayList<>();
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setFavorite(true);
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setFavorite(true);
        vehicleList.add(vehicle1);
        vehicleList.add(vehicle2);

        when(vehicleRepository.findByFavorite()).thenReturn(vehicleList);

        List<Vehicle> actualVehiclesList = vehicleService.getFavoriteVehicles();

        assertAll(
                () -> assertFalse(actualVehiclesList.isEmpty()),
                () -> assertEquals(vehicleList, actualVehiclesList),
                () -> assertEquals(vehicleList.size(), actualVehiclesList.size()),

                () -> verify(vehicleRepository).findByFavorite()
        );
    }

    @Test
    public void testGetFavoriteVehicles_EmptyList() {
        when(vehicleRepository.findByFavorite()).thenReturn(Collections.emptyList());
        List<Vehicle> vehicleList = vehicleService.getFavoriteVehicles();

        assertAll(
                () -> assertEquals(Collections.emptyList(), vehicleList),
                () -> assertTrue(vehicleList.isEmpty()),

                () -> verifyNoMoreInteractions(vehicleRepository)
        );
    }

    @Test
    public void testAddToFavorites(){
        Long existingId = 1L;
        Vehicle vehicle = new Vehicle();
        vehicle.setId(existingId);

        when(vehicleRepository.findById(existingId)).thenReturn(Optional.of(vehicle));

        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        boolean result = vehicleService.addToFavorites(existingId);

        assertAll(
                () -> assertTrue(result),

                () -> verify(vehicleRepository).findById(existingId),
                () -> verify(vehicleRepository).save(any())
        );

    }

    @Test
    public void testRemoveToFavorites(){
        Long existingId = 1L;
        Vehicle vehicle = new Vehicle();
        vehicle.setId(existingId);

        when(vehicleRepository.findById(existingId)).thenReturn(Optional.of(vehicle));

        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        boolean result = vehicleService.removeFromFavorites(existingId);

        assertAll(
                () -> assertTrue(result),

                () -> verify(vehicleRepository).findById(existingId),
                () -> verify(vehicleRepository).save(any())
        );

    }
}
