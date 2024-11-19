package com.example.CarRentalSystem.serviceUnitTests;

import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.model.VehicleType;
import com.example.CarRentalSystem.repository.SearchRepository;
import com.example.CarRentalSystem.service.SearchServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceImpTest {

    @InjectMocks
    private SearchServiceImp searchService;

    @Mock
    private SearchRepository repository;

    @Test
    public void testGetAvailableVehicle_Successfully(){
        City cityStart = City.BERLIN;
        City cityEnd = City.BERLIN;
        LocalDate dateStart = LocalDate.of(2024, 11, 15);
        LocalDate dateEnd = LocalDate.of(2024, 11, 15);

        Vehicle vehicle = new Vehicle();
        vehicle.setVinCode("75765765");
        vehicle.setVehicleNumber("SGS555");

        List<Vehicle> availableVehicleList = List.of(new Vehicle());

        when(repository.queryByAvailableVehicle(cityStart, dateStart, dateEnd)).thenReturn(availableVehicleList);

        List<Vehicle> resultList = searchService.getAvailableVehicle(cityStart, cityEnd, dateStart, dateEnd);

        assertAll(
                () -> assertEquals(availableVehicleList.size(), resultList.size()),
                () -> assertNotNull(resultList),

                () -> verify(repository).queryByAvailableVehicle(cityStart, dateStart, dateEnd)

        );
    }

    @Test
    public void testGetAvailableVehicle_EmptyList(){
        City cityStart = City.BERLIN;
        City cityEnd = City.BERLIN;
        LocalDate dateStart = LocalDate.of(2024, 11, 15);
        LocalDate dateEnd = LocalDate.of(2024, 11, 15);

        when(repository.queryByAvailableVehicle(cityStart, dateStart, dateEnd)).thenReturn(Collections.emptyList());

        List<Vehicle> resultList = searchService.getAvailableVehicle(cityStart, cityEnd, dateStart, dateEnd);

        assertAll(
                () -> assertEquals(Collections.emptyList(), resultList),
                () -> assertTrue(resultList.isEmpty()),

                () -> verifyNoMoreInteractions(repository)
        );
    }
}
