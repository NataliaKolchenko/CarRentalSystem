package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.enums.EngineType;
import com.example.CarRentalSystem.enums.TransmissionType;
import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.*;
import com.example.CarRentalSystem.model.dto.VehicleRequestDto;
import com.example.CarRentalSystem.repository.JpaVehicleRepository;
import com.example.CarRentalSystem.service.interfaces.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service

public class VehicleServiceImp implements VehicleService {
    private final JpaVehicleRepository vehicleRepository;
    private final VehicleTypeServiceImp typeService;
    private final SubTypeService subTypeService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final BranchService branchService;

    public VehicleServiceImp(JpaVehicleRepository vehicleRepository,VehicleTypeServiceImp typeService,
                             SubTypeService subTypeService, BrandService brandService, ModelService modelService,
                             BranchService branchService) {
        this.vehicleRepository = vehicleRepository;
        this.typeService = typeService;
        this.subTypeService = subTypeService;
        this.brandService = brandService;
        this.modelService = modelService;
        this.branchService = branchService;
    }

    @Override
    public Vehicle create(VehicleRequestDto vehicleRequestDto) {
        List<Vehicle> existingVehicle = vehicleRepository.findByVinCodeAndVehicleNumber(
                vehicleRequestDto.getVinCode(),
                vehicleRequestDto.getVehicleNumber());
        if(!existingVehicle.isEmpty()){
            throw new SubjectAlreadyExistsException(ErrorMessage.VEHICLE_IS_ALREADY_EXIST);
        }

        Vehicle vehicle =  mapDtoToEntity(vehicleRequestDto);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle getById(Long id) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(id);
        return vehicleOpt.orElseThrow(() -> new SubjectNotFoundException(ErrorMessage.VEHICLE_ID_WAS_NOT_FOUND));
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        return vehicleList.isEmpty() ? Collections.emptyList() : vehicleList;
    }

    @Override
    public Vehicle update(Long id, VehicleRequestDto vehicleRequestDto) {
        Vehicle existingVehicle = getById(id);
        Vehicle vehicle = mapDtoToEntity(vehicleRequestDto);

        existingVehicle.setType(vehicle.getType());
        existingVehicle.setSubType(vehicle.getSubType());
        existingVehicle.setActive(vehicle.isActive());
        existingVehicle.setBrand(vehicle.getBrand());
        existingVehicle.setModel(vehicle.getModel());
        existingVehicle.setEngineType(vehicle.getEngineType());
        existingVehicle.setYear(vehicle.getYear());
        existingVehicle.setBranch(vehicle.getBranch());
        existingVehicle.setTransmissionType(vehicle.getTransmissionType());
        existingVehicle.setMileage(vehicle.getMileage());
        existingVehicle.setCity(vehicle.getCity());
        existingVehicle.setFavorite(vehicle.isFavorite());
        existingVehicle.setUpdateDate(LocalDateTime.now());

        return vehicleRepository.save(existingVehicle);
    }

    @Override
    public List<Vehicle> getFavoriteVehicles() {
        List<Vehicle> favoriteVehicles = vehicleRepository.findByFavorite();
        return favoriteVehicles.isEmpty() ? Collections.emptyList() : favoriteVehicles;
    }

    public Vehicle mapDtoToEntity(VehicleRequestDto vehicleRequestDto){
        VehicleType type = typeService.getById(vehicleRequestDto.getTypeId());
        SubType subType = subTypeService.getById(vehicleRequestDto.getSubTypeId());
        boolean active = vehicleRequestDto.isActive();
        Brand brand = brandService.getById(vehicleRequestDto.getBrandId());
        Model model = modelService.getById(vehicleRequestDto.getModelId());
        EngineType engineType = vehicleRequestDto.getEngineType();
        int year = vehicleRequestDto.getYear();
        Branch branch = branchService.getById(vehicleRequestDto.getBranchId());
        TransmissionType transmissionType = vehicleRequestDto.getTransmissionType();
        int mileage = vehicleRequestDto.getMileage();
        City city = vehicleRequestDto.getCity();
        boolean favorite = vehicleRequestDto.isFavorite();
        String vinCode = vehicleRequestDto.getVinCode();
        String vehicleNumber = vehicleRequestDto.getVehicleNumber();

        Vehicle vehicle = new Vehicle(type, subType, active, brand, model, engineType, year, branch, transmissionType,
                mileage, city, favorite, vinCode, vehicleNumber);

        return vehicle;
    }




}
