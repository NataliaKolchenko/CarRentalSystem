package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;



public interface SearchRepository{
  @Query("SELECT v FROM Vehicle v LEFT JOIN Booking b ON v.id = b.vehicle.id " +
         "AND (b.bookedFromDate <= :dateStart AND b.bookedToDate >= :dateEnd) " +
         "WHERE v.active = true AND v.city = :cityStart " +
         "AND (b.id IS NULL OR b.status = 'FINISHED')")
 List<Vehicle> queryByAvailableVehicle(
        @Param("cityStart") City cityStart,
        @Param("dateStart") LocalDate dateStart,
        @Param("dateEnd") LocalDate dateEnd);



}
