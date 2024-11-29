package com.example.CarRentalSystem.service.unitTests;


import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.model.entity.SubType;
import com.example.CarRentalSystem.model.entity.VehicleType;
import com.example.CarRentalSystem.repository.JpaSubTypeRepository;
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
public class SubTypeServiceImpTest {
    @InjectMocks
    private SubTypeServiceImp subTypeService;
    @Mock
    private VehicleTypeServiceImp typeService;
    @Mock
    private JpaSubTypeRepository subTypeRepository;

    @Test
    public void testCreate_NewSubType_Successfully(){
        Long existTypeId = 1L;
        String existTypeName = "existTypeName";

        VehicleType type = new VehicleType();
        type.setId(existTypeId);
        type.setVehicleTypeName(existTypeName);

        String subTypeName = "subTypeName";
        SubType subType = new SubType(subTypeName, type);

        when(subTypeRepository.findBySubTypeName(subTypeName)).thenReturn(null);
        when(typeService.getById(existTypeId)).thenReturn(type);
        when(subTypeRepository.save(any(SubType.class))).thenReturn(subType);

        SubType resultSubType = subTypeService.create(subType);

        assertAll(
                () -> assertNotNull(resultSubType),
                () -> assertEquals(subTypeName, resultSubType.getSubTypeName()),

                () -> verify(subTypeRepository).findBySubTypeName(subTypeName),
                () -> verify(typeService).getById(existTypeId),
                () -> verify(subTypeRepository).save(any(SubType.class))
        );

    }

    @Test
    public void testCreate_SubTypeNameNotUnique_ThrowsException(){
        Long existTypeId = 1L;
        String existTypeName = "existTypeName";

        VehicleType existType = new VehicleType();
        existType.setId(existTypeId);
        existType.setVehicleTypeName("TypeName");

        SubType subType = new SubType(existTypeName, existType);

        when(subTypeRepository.findBySubTypeName(existTypeName)).thenReturn(subType);

        SubjectAlreadyExistsException exception = assertThrows(SubjectAlreadyExistsException.class, () ->
                subTypeService.create(subType));

        assertAll(
                () -> assertEquals("SubTypeName has to be unique", exception.getMessage()),

                () -> verify(subTypeRepository).findBySubTypeName(existTypeName),
                () -> verifyNoMoreInteractions(subTypeRepository)
        );
    }

    @Test
    public void testUpdate_ExistingSubType_Successfully(){
        Long existSubTypeId = 1L;
        VehicleType existType = new VehicleType("existTypeName");

        SubType existingSubType = new SubType();
        existingSubType.setId(existSubTypeId);
        existingSubType.setSubTypeName("ExistingSubTypeName");
        existingSubType.setType(existType);

        String newSubTypeName = "NewSubTypeName";
        existingSubType.setSubTypeName(newSubTypeName);

        when(subTypeRepository.findBySubTypeName(existingSubType.getSubTypeName())).thenReturn(null);
        when(subTypeRepository.findById(existSubTypeId)).thenReturn(Optional.of(existingSubType));
        when(subTypeRepository.save(any(SubType.class))).thenReturn(existingSubType);

        SubType resultSubType = subTypeService.update(existSubTypeId, newSubTypeName);

        assertAll(
                () -> assertEquals(resultSubType, existingSubType),
                () -> assertEquals(resultSubType.getSubTypeName(), newSubTypeName),

                () -> verify(subTypeRepository).findBySubTypeName(newSubTypeName),
                () -> verify(subTypeRepository).save(existingSubType)
        );

    }

    @Test
    public void testUpdate_NotUniqueSubType_ThrowsException(){
        Long existSubTypeId = 1L;
        VehicleType existType = new VehicleType("existTypeName");

        SubType existingSubType = new SubType();
        existingSubType.setId(existSubTypeId);
        existingSubType.setSubTypeName("ExistingSubTypeName");
        existingSubType.setType(existType);

        String newSubTypeName = "ExistingSubTypeName";
        existingSubType.setSubTypeName(newSubTypeName);

        when(subTypeRepository.findBySubTypeName(existingSubType.getSubTypeName())).thenReturn(existingSubType);
        when(subTypeRepository.findById(existSubTypeId)).thenReturn(Optional.of(existingSubType));

        SubjectAlreadyExistsException exception = assertThrows(SubjectAlreadyExistsException.class, () ->
                subTypeService.update(existSubTypeId, newSubTypeName));
        assertAll(
                () -> assertEquals("SubTypeName has to be unique", exception.getMessage()),

                () -> verify(subTypeRepository).findBySubTypeName(newSubTypeName),
                () -> verifyNoMoreInteractions(subTypeRepository)
        );
    }

    @Test
    public void testGetById_Successfully(){
        Long subTypeId = 1L;
        SubType expectedSubType = new SubType();
        expectedSubType.setId(subTypeId);
        expectedSubType.setSubTypeName("SubTypeName");
        expectedSubType.setType(new VehicleType("TypeName"));

        when(subTypeRepository.findById(subTypeId)).thenReturn(Optional.of(expectedSubType));

        SubType subTypeById = subTypeService.getById(subTypeId);

        assertAll(
                () -> assertNotNull(subTypeById),
                () -> assertEquals(expectedSubType, subTypeById),

                () -> verify(subTypeRepository).findById(subTypeId)
        );
    }

    @Test
    public void testGeById_NotExistSubTypeId_ThrowsException() {
        Long subTypeId = 1L;

        when(subTypeRepository.findById(subTypeId)).thenReturn(Optional.empty());

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> subTypeService.getById(subTypeId));

        assertAll(
                () -> assertEquals("SubTypeId was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(subTypeRepository)
        );

    }

    @Test
    public void testGeByName_Successfully() {
        String SubTypeName = "expectedSubType";
        SubType expectedSubType = new SubType(SubTypeName, new VehicleType("Type"));

        when(subTypeRepository.findBySubTypeName(SubTypeName)).thenReturn(expectedSubType);

        SubType subTypeByName = subTypeService.getByName(SubTypeName);

        assertAll(
                () -> assertNotNull(subTypeByName),
                () -> assertEquals(expectedSubType, subTypeByName),

                () -> verify(subTypeRepository).findBySubTypeName(SubTypeName)
        );
    }

    @Test
    public void testGetByName_NotExistSubTypeId_ThrowsException() {
        String subTypeName = "expectedSubType";
        when(subTypeRepository.findBySubTypeName(subTypeName)).thenReturn(null);

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> subTypeService.getByName(subTypeName));

        assertAll(
                () -> assertEquals("SubTypeName was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(subTypeRepository)
        );
    }

    @Test
    public void testGetAll_Successfully() {
        List<SubType> subTypeList = new ArrayList<>();
        VehicleType type = new VehicleType("Type");
        SubType subType1 = new SubType("SubType1", type);
        SubType subType2 = new SubType("SubType2", type);
        subTypeList.add(subType1);
        subTypeList.add(subType2);

        when(subTypeRepository.findAll()).thenReturn(subTypeList);

        List<SubType> actualSubTypeList = subTypeService.getAllSubTypes();

        assertAll(
                () -> assertFalse(actualSubTypeList.isEmpty()),
                () -> assertEquals(subTypeList, actualSubTypeList),
                () -> assertEquals(subTypeList.size(), actualSubTypeList.size()),

                () -> verify(subTypeRepository).findAll()
        );
    }

    @Test
    public void testGetAllSubTypes_EmptyList() {
        when(subTypeRepository.findAll()).thenReturn(Collections.emptyList());
        List<SubType> subTypeList = subTypeService.getAllSubTypes();

        assertAll(
                () -> assertEquals(Collections.emptyList(), subTypeList),
                () -> assertTrue(subTypeList.isEmpty()),

                () -> verifyNoMoreInteractions(subTypeRepository)
        );
    }

    @Test
    public void testDeleteById_SubTypeIdIsExist_Successfully() {
        Long subTypeID = 1L;
        when(subTypeRepository.existsById(subTypeID)).thenReturn(true);

        subTypeService.deleteById(subTypeID);

        verify(subTypeRepository).deleteById(subTypeID);
    }

    @Test
    public void testDeleteById_SubTypeIdNotExist_ThrowsException() {
        Long subTypeID = 1L;
        when(subTypeRepository.existsById(subTypeID)).thenReturn(false);

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> subTypeService.deleteById(subTypeID));

        assertAll(
                () -> assertEquals("SubTypeId was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(subTypeRepository)
        );

    }
    @Test
    public void testExistsByVehicleTypeId_true() {
        Long typeId = 1L;
        VehicleType type = new VehicleType("Type");
        type.setId(typeId);

        SubType subType = new SubType("Subtype", type);

        List<SubType> subTypeList = List.of(subType);

        when(subTypeRepository.findByTypeId(typeId)).thenReturn(subTypeList);
        boolean result = subTypeService.existsByVehicleTypeId(typeId);

        assertAll(
                () -> assertTrue(result),

                () -> verify(subTypeRepository).findByTypeId(typeId)
        );
    }

    @Test
    public void testExistsByVehicleTypeId_false() {
        Long typeId = 1L;

        List<SubType> subTypeList = new ArrayList<>();

        when(subTypeRepository.findByTypeId(typeId)).thenReturn(subTypeList);
        boolean result = subTypeService.existsByVehicleTypeId(typeId);

        assertAll(
                () -> assertFalse(result),

                () -> verify(subTypeRepository).findByTypeId(typeId)
        );
    }
}

