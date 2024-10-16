package com.example.CarRentalSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SubType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @NotBlank(message = "modelName may not be blank or null or has spaces")
    private String vehicleSubTypeName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    @NotNull(message = "Brand may not be null")
    private VehicleType vehicleType;

    public SubType() {
    }

    public SubType(String vehicleSubTypeName, VehicleType vehicleType) {
        this.vehicleSubTypeName = vehicleSubTypeName;
        this.vehicleType = vehicleType;
    }
}
