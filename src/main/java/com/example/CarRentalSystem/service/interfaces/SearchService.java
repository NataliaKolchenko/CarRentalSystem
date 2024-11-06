package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.model.Vehicle;

import java.time.LocalDate;
import java.util.List;

public interface SearchService {
    List<Vehicle> getAvailableVehicle(City cityStart,
                                      City cityEnd,
                                      LocalDate dateStart,
                                      LocalDate dateEnd);
}
