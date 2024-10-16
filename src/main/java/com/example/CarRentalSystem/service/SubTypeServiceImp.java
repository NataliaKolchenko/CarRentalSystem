package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.model.SubType;
import com.example.CarRentalSystem.model.VehicleType;
import com.example.CarRentalSystem.repository.JpaSubTypeRepository;
import com.example.CarRentalSystem.service.interfaces.SubTypeService;
import com.example.CarRentalSystem.service.interfaces.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class SubTypeServiceImp implements SubTypeService {
    private final JpaSubTypeRepository subTypeRepository;
    private final VehicleTypeService typeService;

    @Autowired
    public SubTypeServiceImp(JpaSubTypeRepository subTypeRepository, VehicleTypeService typeService) {
        this.subTypeRepository = subTypeRepository;
        this.typeService = typeService;
    }


    @Override
    public SubType create(SubType subTypeName) {
        SubType checkExistSubType = subTypeRepository.findBySubTypeName(subTypeName.getSubTypeName());
        if(checkExistSubType != null){
            throw  new SubjectAlreadyExistsException("SubTypeName has to be unique");
        }
        VehicleType type = typeService.getById(subTypeName.getType().getId());
        SubType newSubType = new SubType(subTypeName.getSubTypeName(), type);

        return subTypeRepository.save(newSubType);
    }

    @Override
    public SubType update(Long subTypeId, String newSubTypeName) {
        SubType subType = getById(subTypeId);
        if(subTypeRepository.findBySubTypeName(newSubTypeName) != null){
            throw new SubjectAlreadyExistsException("SubTypeName has to be unique");
        }
        subType.setSubTypeName(newSubTypeName);
        SubType updatedSubType = subTypeRepository.save(subType);
        return updatedSubType;
    }

    @Override
    public void deleteById(Long subTypeId) {
        if(!subTypeRepository.existsById(subTypeId)){
            throw new SubjectNotFoundException("subTypeId was not found");
        }
        subTypeRepository.deleteById(subTypeId);
    }

    @Override
    public SubType getById(Long subTypeId) {
        Optional<SubType> subTypeOpt = subTypeRepository.findById(subTypeId);
        SubType subType = subTypeOpt.orElseThrow(() -> new SubjectNotFoundException("subTypeId was not found"));
        return subType;
    }

    @Override
    public SubType getByName(String subTypeName) {
        SubType subType = subTypeRepository.findBySubTypeName(subTypeName);
        if (subType == null){
            throw new SubjectNotFoundException("subTypeName was not found");
        }
        return subType;
    }

    @Override
    public List<SubType> getAllSubTypes() {
        List<SubType> subTypeList = subTypeRepository.findAll();
        return subTypeList.isEmpty() ? Collections.emptyList() : subTypeList;
    }
}
