package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.service.interfaces.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/searchService")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;

    }

    @GetMapping("/searchVehicleByQuery")
    public ResponseEntity<List<Vehicle>> getAvailableVehicle(
            @RequestParam City cityStart,
            @RequestParam City cityEnd,
            @RequestParam LocalDate dateStart,
            @RequestParam LocalDate dateEnd) {

        return ResponseEntity.ok(searchService.getAvailableVehicle(cityStart, cityEnd, dateStart, dateEnd));
    }
}
