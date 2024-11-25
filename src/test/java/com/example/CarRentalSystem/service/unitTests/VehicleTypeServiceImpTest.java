package com.example.CarRentalSystem.service.unitTests;

import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotBeDeletedException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.model.VehicleType;
import com.example.CarRentalSystem.repository.JpaVehicleTypeRepository;
import com.example.CarRentalSystem.service.SubTypeServiceImp;
import com.example.CarRentalSystem.service.VehicleTypeServiceImp;
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
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class VehicleTypeServiceImpTest {

    @InjectMocks
    private VehicleTypeServiceImp typeService;
    @Mock
    private JpaVehicleTypeRepository typeRepository;
    @Mock
    private SubTypeServiceImp subTypeService;

    @Test
    public void testCreate_NewType_Successfully() {
        String typeName = "Test";

        when(typeRepository.findByVehicleTypeName(typeName)).thenReturn(null);
        when(typeRepository.save(any(VehicleType.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VehicleType result = typeService.create(typeName);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(typeName, result.getVehicleTypeName()),

                () -> verify(typeRepository).findByVehicleTypeName(typeName),
                () -> verify(typeRepository).save(any(VehicleType.class))
        );
    }

    @Test
    public void testCreate_ExistingType_ThrowsException() {
        String existingTypeName = "ExistingType";
        VehicleType existingType = new VehicleType(existingTypeName);

        when(typeRepository.findByVehicleTypeName(existingTypeName)).thenReturn(existingType);

        SubjectAlreadyExistsException exception = assertThrows(SubjectAlreadyExistsException.class, () ->
                typeService.create(existingTypeName));

        assertAll(
                () -> assertEquals("VehicleTypeName has to be unique", exception.getMessage()),

                () -> verify(typeRepository).findByVehicleTypeName(existingTypeName),
                () -> verifyNoMoreInteractions(typeRepository)
        );
    }

    @Test
    public void testUpdate_ExistingType_Successfully() {
        Long existingId = 1L;
        String newTypeName = "newTypeName";

        VehicleType existingType = new VehicleType();
        existingType.setId(existingId);
        existingType.setVehicleTypeName("Type");

        when(typeRepository.findById(existingId)).thenReturn(Optional.of(existingType));
        when(typeRepository.findByVehicleTypeName(newTypeName)).thenReturn(null);

        VehicleType updatedType = new VehicleType();
        updatedType.setId(existingId);
        updatedType.setVehicleTypeName(newTypeName);

        when(typeRepository.save(existingType)).thenReturn(updatedType);
        VehicleType result = typeService.update(existingId, newTypeName);

        assertAll(
                () -> assertEquals(result, updatedType),
                () -> assertEquals(result.getVehicleTypeName(), newTypeName),

                () -> verify(typeRepository).findByVehicleTypeName(newTypeName),
                () -> verify(typeRepository).save(existingType)
        );
    }

    @Test
    public void testUpdate_NotUniqueType_ThrowsException() {
        Long existingId = 1L;
        String newTypeName = "ExistingType";

        VehicleType existingType = new VehicleType();
        existingType.setId(existingId);
        existingType.setVehicleTypeName("ExistingType");

        when(typeRepository.findById(existingId)).thenReturn(Optional.of(existingType));
        when(typeRepository.findByVehicleTypeName(newTypeName)).thenReturn(existingType);

        VehicleType updatedType = new VehicleType();
        updatedType.setId(existingId);
        updatedType.setVehicleTypeName(newTypeName);

        SubjectAlreadyExistsException exception = assertThrows(SubjectAlreadyExistsException.class, () ->
                typeService.update(existingId, newTypeName));
        assertAll(
                () -> assertEquals("VehicleTypeName has to be unique", exception.getMessage()),

                () -> verify(typeRepository).findByVehicleTypeName(newTypeName),
                () -> verifyNoMoreInteractions(typeRepository)
        );
    }

    @Test
    public void testGetById_Successfully() {
        Long typeId = 1L;
        VehicleType expectedType = new VehicleType("ExpectedType");
        when(typeRepository.findById(typeId)).thenReturn(Optional.of(expectedType));

        VehicleType typeById = typeService.getById(typeId);

        assertAll(
                () -> assertNotNull(typeById),
                () -> assertEquals(expectedType, typeById),

                () -> verify(typeRepository).findById(typeId)
        );


    }

    @Test
    public void testGetById_NotExistTypeId_ThrowsException() {
        Long typeId = 1L;
        when(typeRepository.findById(typeId)).thenReturn(Optional.empty());

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> typeService.getById(typeId));

        assertAll(
                () -> assertEquals("VehicleTypeId was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(typeRepository)
        );

    }

    @Test
    public void testGetTypeByName_Successfully() {
        String typeName = "expectedTypeName";
        VehicleType expectedType = new VehicleType("expectedTypeName");

        when(typeRepository.findByVehicleTypeName(typeName)).thenReturn(expectedType);

        VehicleType typeByName = typeService.getByName(typeName);

        assertAll(
                () -> assertNotNull(typeByName),
                () -> assertEquals(expectedType, typeByName),

                () -> verify(typeRepository).findByVehicleTypeName(typeName)
        );


    }

    @Test
    public void testGetTypeByName_NotExistTypeName_ThrowsException() {
        String typeName = "expectedTypeName";
        when(typeRepository.findByVehicleTypeName(typeName)).thenReturn(null);

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> typeService.getByName(typeName));

        assertAll(
                () -> assertEquals("VehicleTypeName was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(typeRepository)
        );
    }

    @Test
    public void testGetAllTypes_Successfully() {
        List<VehicleType> typeList = new ArrayList<>();
        VehicleType type1 = new VehicleType("Type1");
        VehicleType type2 = new VehicleType("Type2");

        typeList.add(type1);
        typeList.add(type2);

        when(typeRepository.findAll()).thenReturn(typeList);

        List<VehicleType> actualTypeList = typeService.getAll();

        assertAll(
                () -> assertFalse(actualTypeList.isEmpty()),
                () -> assertEquals(typeList, actualTypeList),
                () -> assertEquals(typeList.size(), actualTypeList.size()),

                () -> verify(typeRepository).findAll()
        );


    }

    @Test
    public void testGetAllTypes_EmptyList() {
        when(typeRepository.findAll()).thenReturn(Collections.emptyList());

        List<VehicleType> typeList = typeService.getAll();

        assertAll(
                () -> assertEquals(Collections.emptyList(), typeList),
                () -> assertTrue(typeList.isEmpty()),

                () -> verifyNoMoreInteractions(typeRepository)
        );
    }

    @Test
    public void testDeleteById_TypeExistAndNoSubtypes_Successfully(){
        Long typeId = 1L;

        when(typeRepository.existsById(typeId)).thenReturn(true);
        when(subTypeService.existsByVehicleTypeId(typeId)).thenReturn(false);

        typeService.deleteById(typeId);

        verify(typeRepository).deleteById(typeId);
    }

    @Test
    public void testDeleteById_TypeIdNotExist_ThrowsException(){
        Long typeId = 1L;
        when(typeRepository.existsById(typeId)).thenReturn(false);

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> typeService.deleteById(typeId));

        assertAll(
                () -> assertEquals("VehicleTypeId was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(typeRepository)
        );
    }

    @Test
    public void testDeleteById_TypeHasSubtypes_ThrowsException(){
        Long typeId = 1L;
        when(typeRepository.existsById(typeId)).thenReturn(true);
        when(subTypeService.existsByVehicleTypeId(typeId)).thenReturn(true);

        SubjectNotBeDeletedException exception = assertThrows(SubjectNotBeDeletedException.class,
                () -> typeService.deleteById(typeId));

        assertAll(
                () -> assertEquals("Can't delete vehicle type with associated subtypes", exception.getMessage()),

                () -> verifyNoMoreInteractions(typeRepository)
        );

    }
}

