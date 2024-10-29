package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.model.VehicleDoc;
import com.example.CarRentalSystem.repository.JpaVehicleRepository;
import com.example.CarRentalSystem.service.interfaces.VehicleDocService;
import com.example.CarRentalSystem.service.interfaces.VehicleService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class VehicleServiceImp implements VehicleService {
    private final JpaVehicleRepository vehicleRepository;
    private final VehicleDocServiceImp docService;

    public VehicleServiceImp(JpaVehicleRepository vehicleRepository, VehicleDocServiceImp docService) {
        this.vehicleRepository = vehicleRepository;
        this.docService = docService;
    }

    @Override
    public Vehicle create(Vehicle vehicle) {
        VehicleDoc doc = docService.create(new VehicleDoc(
                vehicle.getVehicleDocs().get(0).getVehicleDocType(),
                vehicle.getVehicleDocs().get(0).getLink(),
                vehicle.getVehicleDocs().get(0).getVehicle()));
        List<VehicleDoc> docList = new ArrayList<>();
        docList.add(doc);

        Vehicle newVehicle = new Vehicle(
                vehicle.getType(),
                vehicle.getSubType(),
                vehicle.isActive(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getEngineType(),
                vehicle.getYear(),
                vehicle.getBranch(),
                vehicle.getTransmissionType(),
                vehicle.getMileage(),
                docList,
                vehicle.getCity(),
                vehicle.isFavorite()
        );


        return vehicleRepository.save(newVehicle);
    }
}
