package com.example.CarRentalSystem.model.dto;

import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.enums.EngineType;
import com.example.CarRentalSystem.enums.TransmissionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequestDto {
    private Long typeId;
    private Long subTypeId;
    private boolean active;
    private Long brandId;
    private Long modelId;
    private EngineType engineType;
    private int year;
    private Long branchId;
    private TransmissionType transmissionType;
    private int mileage;
    private City city;
    boolean favorite;
    private String vinCode;
    private String vehicleNumber;
}
