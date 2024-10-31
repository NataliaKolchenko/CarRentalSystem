package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaBookingRepository extends JpaRepository<Booking, Long> {
//    @Query("SELECT b FROM Booking b WHERE b.vehicle_id = :vehicleId " +
//            "AND b.booked_from_date <= :endDate " +
//            "AND b.booked_to_date >= :startDate " +
//            "AND b.booking_status <> :status")
//    List<Booking> findBookingsWithinDateRangeAndStatusNot(
//            @Param("vehicleId") Long vehicleId,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate,
//            @Param("status") BookingStatus status);
}
