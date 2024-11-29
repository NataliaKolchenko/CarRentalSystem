package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.enums.City;
import com.example.CarRentalSystem.model.entity.Vehicle;

import java.time.LocalDate;
import java.util.List;

public interface SearchService {
    List<Vehicle> getAvailableVehicle(City cityStart,
                                      City cityEnd,
                                      LocalDate dateStart,
                                      LocalDate dateEnd);
}
