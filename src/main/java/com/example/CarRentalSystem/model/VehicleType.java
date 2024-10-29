package com.example.CarRentalSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class VehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "vehicleType may not be blank or null or has spaces")
    private String vehicleTypeName;

    public VehicleType() {
    }

    public VehicleType(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public VehicleType(Long id) {
        this.id = id;
    }
}
