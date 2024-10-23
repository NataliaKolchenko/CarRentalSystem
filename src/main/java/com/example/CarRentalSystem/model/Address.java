package com.example.CarRentalSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    Long id;

    String zipCode;

    @NotBlank(message = "Country may not be blank")
    String country;

    String region;

    @NotBlank(message = "City may not be blank")
    String city;

    String district;

    @NotBlank(message = "Street may not be blank")
    String street;

    @Positive(message = "HouseNumber may not be blank")
    int house;

    @NotBlank(message = "ApartmentNumber may not be blank")
    String apartment;

    String additionalInfo;

    LocalDateTime createDate;

    LocalDateTime updateDate;

    Long changeUserId;

    public Address() {
        this.createDate = LocalDateTime.now();
    }

    public Address(Long id, String zipCode, String country, String region, String city, String district,
                   String street, int house, String apartment, String additionalInfo, LocalDateTime createDate,
                   LocalDateTime updateDate, Long changeUserId) {
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
        this.updateDate = updateDate;
        this.changeUserId = changeUserId;
    }
}
