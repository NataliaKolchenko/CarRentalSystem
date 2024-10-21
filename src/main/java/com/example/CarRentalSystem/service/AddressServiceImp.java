package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.exception.AddressIsNotFull;
import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.model.Address;
import com.example.CarRentalSystem.repository.JpaAddressRepository;
import com.example.CarRentalSystem.service.interfaces.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AddressServiceImp  implements AddressService {
    private final JpaAddressRepository addressRepository;

    @Autowired
    public AddressServiceImp(JpaAddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address create(Address address) {
        Address checkExistAddress = addressRepository.findByCountryAndCityAndStreet(address.getCountry(),
                address.getCity(), address.getStreet());
        if(checkExistAddress != null){
            throw new SubjectAlreadyExistsException("Address is already exist");
        }
        if (address.getHouse() == 0 && address.getApartment().isEmpty())  {
            throw new AddressIsNotFull("Address isn't full. Please add HouseNumber or ApartmentNumber");
        }
//  !!!добавить проброс UserId
        Address newAddress = Address.builder()
                .zipCode(address.getZipCode())
                .country(address.getCountry())
                .region(address.getRegion())
                .city(address.getCity())
                .district(address.getDistrict())
                .street(address.getStreet())
                .house(address.getHouse())
                .apartment(address.getApartment())
                .additionalInfo(address.getAdditionalInfo())
                .build();
        addressRepository.save(newAddress);
        return newAddress;
    }

    @Override
    public Address update(Long id, Address newAddress) {
        return null;
    }

    @Override
    public Address getById(Long id) {
        return null;
    }
}
