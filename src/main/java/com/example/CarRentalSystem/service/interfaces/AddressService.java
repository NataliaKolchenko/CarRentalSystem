package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.Address;

public interface AddressService {
    Address create(Address address);
    Address update(Long id, Address newAddress);
    Address getById(Long id);
}