package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.enums.EngineType;
import com.example.CarRentalSystem.enums.TransmissionType;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.model.*;
import com.example.CarRentalSystem.repository.JpaVehicleRepository;
import com.example.CarRentalSystem.service.interfaces.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class VehicleServiceImp implements VehicleService {
    private final JpaVehicleRepository vehicleRepository;
    private final VehicleDocServiceImp docService;
    private final VehicleTypeServiceImp typeService;
    private final SubTypeService subTypeService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final BranchService branchService;

    public VehicleServiceImp(JpaVehicleRepository vehicleRepository, VehicleDocServiceImp docService, VehicleTypeServiceImp typeService, SubTypeService subTypeService, BrandService brandService, ModelService modelService, BranchService branchService) {
        this.vehicleRepository = vehicleRepository;
        this.docService = docService;
        this.typeService = typeService;
        this.subTypeService = subTypeService;
        this.brandService = brandService;
        this.modelService = modelService;
        this.branchService = branchService;
    }

    @Override
    public Vehicle create(Vehicle vehicle) {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        Long id = savedVehicle.getId();
        VehicleType type = typeService.getById(vehicle.getType().getId());
        SubType subType = subTypeService.getById(vehicle.getSubType().getId());
        boolean active = savedVehicle.isActive();
        Brand brand = brandService.getVehicleBrandById(vehicle.getBrand().getId());
        Model model = modelService.getModelById(vehicle.getModel().getId());
        EngineType engineType = vehicle.getEngineType();
        int year = vehicle.getYear();
        Branch branch = branchService.getById(vehicle.getBranch().getId());
        TransmissionType transmissionType = vehicle.getTransmissionType();
        int mileage = vehicle.getMileage();
        List<VehicleDoc> vehicleDocs = savedVehicle.getVehicleDocs();
        String city = vehicle.getCity();
        boolean favorite = vehicle.isFavorite();

        Vehicle newVehicle = new Vehicle(type, subType, active, brand, model, engineType, year, branch,
                transmissionType, mileage, vehicleDocs, city, favorite);
        newVehicle.setId(id);

        return newVehicle;
    }

    public Vehicle getById(Long vehicleId) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        Vehicle vehicle = vehicleOpt.orElseThrow(() -> new SubjectNotFoundException("VehicleId was not wound"));
        return vehicle;
    }
}
