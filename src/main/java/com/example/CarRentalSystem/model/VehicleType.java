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
    @Column
    private Long id;

    @Column
    @NotBlank(message = "vehicleType may not be blank or null or has spaces")
    private String vehicleType;

    public VehicleType() {
    }

    public VehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
