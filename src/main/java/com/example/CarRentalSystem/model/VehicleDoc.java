package com.example.CarRentalSystem.model;

import com.example.CarRentalSystem.enums.VehicleDocType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class VehicleDoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "vehicleDocType may not be blank or null or has spaces")
    private VehicleDocType vehicleDocType;

    @URL
    @NotBlank
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    @NotBlank(message = "vehicleId may not be blank or null or has spaces")
    private Vehicle vehicle;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    public VehicleDoc() {
        this.createDate = LocalDateTime.now();
    }

    public VehicleDoc(VehicleDocType vehicleDocType, String link, Vehicle vehicle) {
        this.vehicleDocType = vehicleDocType;
        this.link = link;
        this.vehicle = vehicle;
        this.createDate = LocalDateTime.now();
    }
}
