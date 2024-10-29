package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.enums.EngineType;
import com.example.CarRentalSystem.enums.TransmissionType;
import com.example.CarRentalSystem.model.*;
import com.example.CarRentalSystem.model.dto.VehicleRequestDto;
import com.example.CarRentalSystem.repository.JpaVehicleRepository;
import com.example.CarRentalSystem.service.interfaces.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class VehicleServiceImp implements VehicleService {
    private final JpaVehicleRepository vehicleRepository;
    private final VehicleTypeServiceImp typeService;
    private final SubTypeService subTypeService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final BranchService branchService;

    public VehicleServiceImp(JpaVehicleRepository vehicleRepository,VehicleTypeServiceImp typeService, SubTypeService subTypeService, BrandService brandService, ModelService modelService, BranchService branchService) {
        this.vehicleRepository = vehicleRepository;
        this.typeService = typeService;
        this.subTypeService = subTypeService;
        this.brandService = brandService;
        this.modelService = modelService;
        this.branchService = branchService;
    }

    @Override
    public Vehicle create(VehicleRequestDto vehicleRequestDto) {

       VehicleType type = typeService.getById(vehicleRequestDto.getTypeId());
       SubType subType = subTypeService.getById(vehicleRequestDto.getSubTypeId());
       boolean active = vehicleRequestDto.isActive();
       Brand brand = brandService.getVehicleBrandById(vehicleRequestDto.getBrandId());
       Model model = modelService.getModelById(vehicleRequestDto.getModelId());
       EngineType engineType = vehicleRequestDto.getEngineType();
       int year = vehicleRequestDto.getYear();
       Branch branch = branchService.getById(vehicleRequestDto.getBranchId());
       TransmissionType transmissionType = vehicleRequestDto.getTransmissionType();
       int mileage = vehicleRequestDto.getMileage();
       String city = vehicleRequestDto.getCity();
       boolean favorite = vehicleRequestDto.isFavorite();

       Vehicle vehicle = new Vehicle(type, subType, active, brand, model, engineType, year, branch, transmissionType,
               mileage, city, favorite);

        return vehicleRepository.save(vehicle);
    }

}
