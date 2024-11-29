package com.example.CarRentalSystem.model.entity;

import com.example.CarRentalSystem.model.enums.City;
import com.example.CarRentalSystem.model.enums.EngineType;
import com.example.CarRentalSystem.model.enums.TransmissionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_type_id")
    @NotNull(message = "VehicleType may not be null")
    private VehicleType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sub_type_id")
    @NotNull(message = "SubType may not be null")
    private SubType subType;

//    Состояние ТС
//    активен (true)- доступен для отображения в каталоге,
//    неактивен (false) - выведен из эксплуатации
    private boolean active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    @NotNull(message = "brandId may not be null")
    private Brand brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id")
    @NotNull(message = "modelId may not be null")
    private Model model;

    @NotNull(message = "engineType may not be null")
    @Enumerated(EnumType.STRING)
    private EngineType engineType;

    @Positive(message = "year may not be less then 1950")
    @Min(1950L)
//    @NotNull(message = "year may not be null")
    private int year;

    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "branch_id")
    @NotNull(message = "branchId may not be null")
    private Branch branch;

    @NotNull(message = "transmissionType may not be null")
    @Enumerated(EnumType.STRING)
    private TransmissionType transmissionType;

    @PositiveOrZero(message = "mileage may not be negative")
//    @NotNull(message = "mileage may not be null")
    private int mileage;

    @NotNull(message = "city may not be blank")
    @Enumerated(EnumType.STRING)
    private City city;

    boolean favorite;

    @NotBlank(message = "vinCode may not be blank")
    private String vinCode;

    @NotBlank(message = "vehicleNumber may not be blank")
    private String vehicleNumber;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    public Vehicle(LocalDateTime createDate) {
        this.createDate = LocalDateTime.now();
    }

    public Vehicle() {
        this.createDate = LocalDateTime.now();
    }

    public Vehicle(VehicleType type, SubType subType, boolean active, Brand brand, Model model, EngineType engineType,
                   int year, Branch branch, TransmissionType transmissionType, int mileage, City city, boolean favorite,
                   String vinCode, String vehicleNumber) {
        this.type = type;
        this.subType = subType;
        this.active = active;
        this.brand = brand;
        this.model = model;
        this.engineType = engineType;
        this.year = year;
        this.branch = branch;
        this.transmissionType = transmissionType;
        this.mileage = mileage;
        this.city = city;
        this.favorite = favorite;
        this.vinCode = vinCode;
        this.vehicleNumber = vehicleNumber;
        this.createDate = LocalDateTime.now();
    }
}
