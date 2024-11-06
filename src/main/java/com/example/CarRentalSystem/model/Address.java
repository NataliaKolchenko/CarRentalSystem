package com.example.CarRentalSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@EqualsAndHashCode
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zipCode;

    @NotBlank(message = "Country may not be blank")
    private String country;

    private String region;

    @NotBlank(message = "City may not be blank")
    private String city;

    private String district;

    @NotBlank(message = "Street may not be blank")
    private String street;

    @Positive(message = "HouseNumber may not be blank")
    private int house;

    @NotBlank(message = "ApartmentNumber may not be blank")
    private String apartment;

    private String additionalInfo;

    private LocalDateTime createDate;

    public Address() {
        this.createDate = LocalDateTime.now();
    }

    public Address(Long id, String zipCode, String country, String region, String city, String district,
                   String street, int house, String apartment, String additionalInfo, LocalDateTime createDate) {
        this.id = id;
        this.zipCode = zipCode;
        this.country = country;
        this.region = region;
        this.city = city;
        this.district = district;
        this.street = street;
        this.house = house;
        this.apartment = apartment;
        this.additionalInfo = additionalInfo;
        this.createDate = LocalDateTime.now();
    }

}
