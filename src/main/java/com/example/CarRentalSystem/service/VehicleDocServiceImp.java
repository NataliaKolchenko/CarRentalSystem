//package com.example.CarRentalSystem.service;
//
//import com.example.CarRentalSystem.model.VehicleDoc;
//import com.example.CarRentalSystem.model.VehicleType;
//import com.example.CarRentalSystem.repository.JpaVehicleDocRepository;
//import com.example.CarRentalSystem.service.interfaces.VehicleDocService;
//import org.springframework.stereotype.Service;
//import org.springframework.validation.annotation.Validated;
//
//@Service
//@Validated
//public class VehicleDocServiceImp implements VehicleDocService {
//    private final JpaVehicleDocRepository docRepository;
//
//    public VehicleDocServiceImp(JpaVehicleDocRepository docRepository) {
//        this.docRepository = docRepository;
//    }
//
//    @Override
//    public VehicleDoc create(VehicleDoc doc) {
//        return docRepository.save(doc);
//    }
//
////    public VehicleDoc setVehicleId(VehicleDoc existingDoc, Long vehicleId){
////
////    }
//}
