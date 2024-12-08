package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.model.enums.City;
import com.example.CarRentalSystem.model.entity.Vehicle;
import com.example.CarRentalSystem.repository.SearchRepository;
import com.example.CarRentalSystem.service.interfaces.SearchService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SearchServiceImp implements SearchService {
    private final SearchRepository searchRepository;

    public SearchServiceImp(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public List<Vehicle> getAvailableVehicle(City cityStart, City cityEnd, LocalDate dateStart, LocalDate dateEnd) {
        return searchRepository.queryByAvailableVehicle(cityStart, dateStart, dateEnd);
    }
}
