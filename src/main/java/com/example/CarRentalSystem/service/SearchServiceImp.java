package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.repository.JpaSearchRepository;
import com.example.CarRentalSystem.service.interfaces.SearchService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SearchServiceImp implements SearchService {
    private final JpaSearchRepository searchRepository;

    public SearchServiceImp(JpaSearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public List<Vehicle> getAvailableVehicle(City cityStart, City cityEnd, LocalDate dateStart, LocalDate dateEnd) {
        return null;
    }
}
