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
    private Long id;

    @NotBlank(message = "subTypeName may not be blank or null or has spaces")
    private String subTypeName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_type_id")
    @NotNull(message = "VehicleType may not be null")
    private VehicleType type;

    public SubType() {
    }

    public SubType(String subTypeName, VehicleType type) {
        this.subTypeName = subTypeName;
        this.type = type;
    }

    public SubType(Long id) {
        this.id = id;
    }
}
