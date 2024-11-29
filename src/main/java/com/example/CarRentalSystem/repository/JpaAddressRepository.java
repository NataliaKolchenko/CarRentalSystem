package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.model.enums.City;
import com.example.CarRentalSystem.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAddressRepository extends JpaRepository<Address, Long> {
    Address findByCountryAndCityAndStreetAndHouseAndApartment(String country, City city,
                                                              String street, int house, String apartment);
}
