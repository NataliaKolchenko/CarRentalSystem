package com.example.CarRentalSystem.model.dto;

import com.example.CarRentalSystem.enums.EngineType;
import com.example.CarRentalSystem.enums.TransmissionType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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
    private String city;
    boolean favorite;
    private String vinCode;
    private String vehicleNumber;
}
