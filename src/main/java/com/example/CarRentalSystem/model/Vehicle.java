package com.example.CarRentalSystem.model;

import com.example.CarRentalSystem.enums.EngineType;
import com.example.CarRentalSystem.enums.TransmissionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_type_id")
    @NotNull(message = "VehicleType may not be null")
    private VehicleType type;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sub_type_id")
    @NotNull(message = "SubType may not be null")
    private SubType subType;

//    Состояние ТС
//    активен (true)- доступен для отображения в каталоге,
//    неактивен (false) - выведен из эксплуатации
    @NotNull(message = "VehicleCondition may not be null")
    private boolean active;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    @NotNull(message = "brandId may not be null")
    private Brand brand;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id")
    @NotNull(message = "modelId may not be null")
    private Model model;

    @NotNull(message = "engineType may not be null")
    private EngineType engineType;

    @Positive(message = "year may not be less then 1950")
    @Min(1950L)
    @NotNull(message = "year may not be null")
    private int year;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id")
    @NotNull(message = "branchId may not be null")
    private Branch branch;

    @NotNull(message = "transmissionType may not be null")
    private TransmissionType transmissionType;

    @PositiveOrZero(message = "mileage may not be negative")
    @NotNull(message = "mileage may not be null")
    private int mileage;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @NotNull(message = "vehicleDocs may not be null")
    private List<VehicleDoc> vehicleDocs;

    @NotBlank(message = "city may not be blank")
    private String city;

    @NotNull(message = "favorite may not be null")
    boolean favorite;

    private LocalDateTime createDate;

    public Vehicle(LocalDateTime createDate) {
        this.createDate = LocalDateTime.now();
    }

    public Vehicle(VehicleType type, SubType subType, boolean active, Brand brand, Model model, EngineType engineType,
                   int year, Branch branch, TransmissionType transmissionType, int mileage, List<VehicleDoc> vehicleDocs, String city, boolean favorite) {
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
        this.vehicleDocs = vehicleDocs;
        this.city = city;
        this.favorite = favorite;
        this.createDate = LocalDateTime.now();
    }
}
