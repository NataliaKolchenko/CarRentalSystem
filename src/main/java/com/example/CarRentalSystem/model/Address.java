package com.example.CarRentalSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column
    String zipCode;

    @Column
    @NotBlank(message = "Country may not be blank")
    String country;

    @Column
    String region;

    @Column
    @NotBlank(message = "City may not be blank")
    String city;

    @Column
    String district;

    @Column
    @NotBlank(message = "Street may not be blank")
    String street;

    @Column
    int house;

    @Column
    String apartment;

    @Column
    String additionalInfo;

    @Column
    LocalDateTime createDate;

    @Column
    LocalDateTime updateDate;

    @Column
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
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.changeUserId = changeUserId;
    }
}