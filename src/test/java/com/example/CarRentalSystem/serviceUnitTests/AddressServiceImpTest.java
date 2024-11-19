package com.example.CarRentalSystem.serviceUnitTests;

import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.exception.SubjectAlreadyExistsException;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.model.Address;
import com.example.CarRentalSystem.repository.JpaAddressRepository;
import com.example.CarRentalSystem.service.AddressServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImpTest {
    @InjectMocks
    private AddressServiceImp addressService;
    @Mock
    private JpaAddressRepository addressRepository;

    @Test
    public void testCreate_NewAddress_Successfully() {
        String zipCode = "14000";
        String country = "country";
        String region = "region";
        City city = City.BERLIN;
        String district = "district";
        String street = "street";
        int house = 1;
        String apartment = "apartment";
        String additionalInfo = "additionalInfo";

        when(addressRepository.findByCountryAndCityAndStreetAndHouseAndApartment(country, city, street, house, apartment))
                .thenReturn(null);

        Address address = Address.builder()
                .zipCode(zipCode)
                .country(country)
                .region(region)
                .city(city)
                .district(district)
                .street(street)
                .house(house)
                .apartment(apartment)
                .additionalInfo(additionalInfo)
                .build();

        when(addressRepository.save(any(Address.class))).thenReturn(address);
        Address result = addressService.create(address);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(address, result),

                () -> verify(addressRepository).findByCountryAndCityAndStreetAndHouseAndApartment(country, city, street, house, apartment),
                () -> verify(addressRepository).save(any(Address.class))
        );

    }

    @Test
    public void testCreate_ExistingAddress_ThrowsException() {
        String country = "country";
        City city = City.BERLIN;
        String street = "street";
        int house = 1;
        String apartment = "apartment";

        Address existingAddress = Address.builder()
                .country(country)
                .city(city)
                .street(street)
                .house(house)
                .apartment(apartment)
                .build();
        when(addressRepository.findByCountryAndCityAndStreetAndHouseAndApartment(country, city, street, house, apartment))
                .thenReturn(existingAddress);

        SubjectAlreadyExistsException exception = assertThrows(SubjectAlreadyExistsException.class, () ->
                addressService.create(existingAddress));

        assertAll(
                () -> assertEquals("Address is already exist", exception.getMessage()),

                () -> verify(addressRepository).findByCountryAndCityAndStreetAndHouseAndApartment(country, city, street, house, apartment),
                () -> verifyNoMoreInteractions(addressRepository)
        );

    }

    @Test
    public void testGetById_Successfully() {
        Long addressId = 1L;
        Address expectedAddress = Address.builder()
                .country("country")
                .city(City.BERLIN)
                .street("street")
                .house(1)
                .apartment("apartment")
                .build();

        when(addressRepository.findById(addressId)).thenReturn(Optional.ofNullable(expectedAddress));

        Address addressById = addressService.getById(addressId);

        assertAll(
                () -> assertNotNull(addressById),
                () -> assertEquals(expectedAddress, addressById),

                () -> verify(addressRepository).findById(addressId),
                () -> verifyNoMoreInteractions(addressRepository)
        );
    }

    @Test
    public void testGetById_NotExistAddressId_ThrowsException() {
        Long addressId = 1L;
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> addressService.getById(addressId));

        assertAll(
                () -> assertEquals("AddressId was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(addressRepository)
        );
    }

    @Test
    public void testUpdate_Successfully(){
        Long addressId = 1L;


        Address existingAddress = Address.builder()
                .id(addressId)
                .country("country")
                .city(City.BERLIN)
                .street("street")
                .house(1)
                .apartment("apartment")
                .build();
//        existingAddress.setId(addressId);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));
        Address updatedAddress = Address.builder()
                .id(addressId)
                .country("New_country")
                .city(City.BERLIN)
                .street("street")
                .house(1)
                .apartment("apartment")
                .build();
//        updatedAddress.setId(addressId);

        when(addressRepository.save(any(Address.class))).thenReturn(updatedAddress);
        Address result = addressService.update(addressId, updatedAddress);

        assertAll(
                () -> assertEquals(updatedAddress, result),

                () -> verify(addressRepository).save(any(Address.class))
        );

    }

}
