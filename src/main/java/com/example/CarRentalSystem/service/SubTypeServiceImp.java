package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.SubType;
import com.example.CarRentalSystem.model.VehicleType;
import com.example.CarRentalSystem.repository.JpaSubTypeRepository;
import com.example.CarRentalSystem.service.interfaces.SubTypeService;
import com.example.CarRentalSystem.service.interfaces.VehicleTypeService;
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
    public SubTypeServiceImp(JpaSubTypeRepository subTypeRepository, VehicleTypeService typeService) {
        this.subTypeRepository = subTypeRepository;
        this.typeService = typeService;
    }


    @Override
    public SubType create(SubType subTypeName) {
        SubType checkExistSubType = subTypeRepository.findBySubTypeName(subTypeName.getSubTypeName());
        if(checkExistSubType != null){
            throw  new SubjectAlreadyExistsException(ErrorMessage.SUB_TYPE_NAME_IS_ALREADY_EXIST);
        }
        VehicleType type = typeService.getById(subTypeName.getType().getId());
        SubType newSubType = new SubType(subTypeName.getSubTypeName(), type);

        return subTypeRepository.save(newSubType);
    }

    @Override
    public SubType update(Long subTypeId, String newSubTypeName) {
        SubType subType = getById(subTypeId);
        if(subTypeRepository.findBySubTypeName(newSubTypeName) != null){
            throw new SubjectAlreadyExistsException(ErrorMessage.SUB_TYPE_NAME_IS_ALREADY_EXIST);
        }
        subType.setSubTypeName(newSubTypeName);
        return subTypeRepository.save(subType);
    }

    @Override
    public void deleteById(Long subTypeId) {
        if(!subTypeRepository.existsById(subTypeId)){
            throw new SubjectNotFoundException(ErrorMessage.SUB_TYPE_ID_WAS_NOT_FOUND);
        }
        subTypeRepository.deleteById(subTypeId);
    }

    @Override
    public SubType getById(Long subTypeId) {
        Optional<SubType> subTypeOpt = subTypeRepository.findById(subTypeId);
        return subTypeOpt.orElseThrow(() -> new SubjectNotFoundException(ErrorMessage.SUB_TYPE_ID_WAS_NOT_FOUND));
    }

    @Override
    public SubType getByName(String subTypeName) {
        SubType subType = subTypeRepository.findBySubTypeName(subTypeName);
        if (subType == null){
            throw new SubjectNotFoundException(ErrorMessage.SUB_TYPE_NAME_WAS_NOT_FOUND);
        }
        return subType;
    }

    @Override
    public List<SubType> getAllSubTypes() {
        List<SubType> subTypeList = subTypeRepository.findAll();
        return subTypeList.isEmpty() ? Collections.emptyList() : subTypeList;
    }
}
