package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.VehicleType;
import com.example.CarRentalSystem.repository.JpaVehicleTypeRepository;
import com.example.CarRentalSystem.service.interfaces.VehicleTypeService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class VehicleTypeServiceImp implements VehicleTypeService {
    private final JpaVehicleTypeRepository vtRepository;
    public VehicleTypeServiceImp(JpaVehicleTypeRepository vtRepository) {
        this.vtRepository = vtRepository;
    }

    @Override
    public VehicleType create(String vehicleTypeName) {
        VehicleType checkExistType = vtRepository.findByVehicleTypeName(vehicleTypeName);
        if(checkExistType != null){
            throw new SubjectAlreadyExistsException(ErrorMessage.TYPE_NAME_IS_ALREADY_EXIST);
        }
        VehicleType newVT = new VehicleType(vehicleTypeName);
        return vtRepository.save(newVT);
    }

    @Override
    public VehicleType update(Long vehicleTypeId, String newVehicleTypeName) {
        VehicleType vt = getById(vehicleTypeId);
        if (vtRepository.findByVehicleTypeName(newVehicleTypeName) != null){
            throw new SubjectAlreadyExistsException(ErrorMessage.TYPE_NAME_IS_ALREADY_EXIST);
        }
        vt.setVehicleTypeName(newVehicleTypeName);
        return vtRepository.save(vt);
    }

    @Override
    public void deleteById(Long vehicleTypeId) {
        if (!vtRepository.existsById(vehicleTypeId)) {
            throw new SubjectNotFoundException(ErrorMessage.TYPE_ID_WAS_NOT_FOUND);
        }
        vtRepository.deleteById(vehicleTypeId);

    }

    @Override
    public VehicleType getById(Long vehicleTypeId) {
        Optional<VehicleType> typeOpt = vtRepository.findById(vehicleTypeId);
        VehicleType type = typeOpt.orElseThrow(() -> new SubjectNotFoundException(ErrorMessage.TYPE_ID_WAS_NOT_FOUND));
        return type;
    }

    @Override
    public VehicleType getByName(String vehicleTypeName) {
        VehicleType type = vtRepository.findByVehicleTypeName(vehicleTypeName);
        if (type == null) {
            throw new SubjectNotFoundException(ErrorMessage.TYPE_NAME_WAS_NOT_FOUND);
        }
        return type;
    }

    @Override
    public List<VehicleType> getAll() {
        List<VehicleType> vehicleTypeList = vtRepository.findAll();
        return vehicleTypeList.isEmpty() ? Collections.emptyList() : vehicleTypeList;
    }
}
