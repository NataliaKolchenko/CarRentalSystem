package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.exception.VehicleTypeAlreadyExistsException;
import com.example.CarRentalSystem.exception.VehicleTypeNotFoundException;
import com.example.CarRentalSystem.model.VehicleType;
import com.example.CarRentalSystem.repository.JpaVehicleTypeRepository;
import com.example.CarRentalSystem.service.interfaces.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class VehicleTypeServiceImp implements VehicleTypeService {
    private final JpaVehicleTypeRepository vtRepository;

    @Autowired
    public VehicleTypeServiceImp(JpaVehicleTypeRepository vtRepository) {
        this.vtRepository = vtRepository;
    }

    @Override
    public VehicleType create(String vehicleTypeName) {
        VehicleType checkExistType = vtRepository.findByVehicleTypeName(vehicleTypeName);
        if(checkExistType != null){
            throw new VehicleTypeAlreadyExistsException("vehicleTypeName has to be unique");
        }
        VehicleType newVT = new VehicleType(vehicleTypeName);
        vtRepository.save(newVT);
        return newVT;
    }

    @Override
    public VehicleType update(Long vehicleTypeId, String newVehicleTypeName) {
        VehicleType vt = getById(vehicleTypeId);
        if (vtRepository.findByVehicleTypeName(newVehicleTypeName) != null){
            throw new VehicleTypeAlreadyExistsException("vehicleTypeName has to be unique");
        }
        vt.setVehicleTypeName(newVehicleTypeName);
        VehicleType updatedVT = vtRepository.save(vt);
        return updatedVT;
    }

    @Override
    public void deleteById(Long vehicleTypeId) {
        if (!vtRepository.existsById(vehicleTypeId)) {
            throw new VehicleTypeNotFoundException("vehicleTypeId was not found");
        }
        vtRepository.deleteById(vehicleTypeId);

    }

    @Override
    public VehicleType getById(Long vehicleTypeId) {
        Optional<VehicleType> typeOpt = vtRepository.findById(vehicleTypeId);
        VehicleType type = typeOpt.orElseThrow(() -> new VehicleTypeNotFoundException("vehicleTypeId was not found"));
        return null;
    }

    @Override
    public VehicleType getByName(String vehicleTypeName) {
        VehicleType type = vtRepository.findByVehicleTypeName(vehicleTypeName);
        if (type == null) {
            throw new VehicleTypeNotFoundException("vehicleTypeName was not found");
        }
        return type;
    }

    @Override
    public List<VehicleType> getAll() {
        List<VehicleType> vehicleTypeList = vtRepository.findAll();
        return vehicleTypeList.isEmpty() ? Collections.emptyList() : vehicleTypeList;
    }
}
