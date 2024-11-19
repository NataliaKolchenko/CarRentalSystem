package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.model.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface SearchRepository {

    @Query("SELECT v " +
            "FROM Vehicle v " +
            "WHERE v.active = true AND v.city = :cityStart " +
            "AND NOT EXISTS (" +
            "    SELECT 1 " +
            "    FROM Booking b " +
            "    WHERE b.vehicle.id = v.id " +
            "      AND b.bookedFromDate <= :dateEnd " +
            "      AND b.bookedToDate >= :dateStart " +
            "      AND b.status IN ('ACTIVE', 'CREATED')" +
            ") ORDER BY 1")
    List<Vehicle> queryByAvailableVehicle(
            @Param("cityStart") City cityStart,
            @Param("dateStart") LocalDate dateStart,
            @Param("dateEnd") LocalDate dateEnd);

}
