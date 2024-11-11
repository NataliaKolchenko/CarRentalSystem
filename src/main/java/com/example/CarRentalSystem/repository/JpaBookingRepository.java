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
    @Query("SELECT b FROM Booking b WHERE b.vehicle.id = :vehicleId " +
            "AND b.bookedFromDate <= :startDate " +
            "AND b.bookedToDate >= :endDate " +
            "AND (b.status <> 'FINISHED' AND b.status <> 'CANCELLED')")
    List<Booking> checkExistingBooking(
            @Param("vehicleId") Long vehicleId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<Booking> findByUserId(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.userId = :userId " +
            "AND b.status = :status")
    List<Booking> findByUserIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") BookingStatus status);

}
