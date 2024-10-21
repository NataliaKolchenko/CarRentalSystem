package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAddressRepository extends JpaRepository<Address, Long> {
    Address findByCountryAndCityAndStreet(String country, String city, String street);
}