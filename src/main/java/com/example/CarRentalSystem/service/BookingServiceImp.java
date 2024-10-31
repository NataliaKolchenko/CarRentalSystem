package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.model.Booking;
import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.repository.JpaBookingRepository;
import com.example.CarRentalSystem.repository.JpaVehicleRepository;
import com.example.CarRentalSystem.service.interfaces.BookingService;
import com.example.CarRentalSystem.service.interfaces.VehicleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Valid
public class BookingServiceImp implements BookingService {
    private final JpaBookingRepository bookingRepository;
    private final VehicleService vehicleService;

    public BookingServiceImp(JpaBookingRepository bookingRepository, VehicleService vehicleService) {
        this.bookingRepository = bookingRepository;
        this.vehicleService = vehicleService;
    }

    @Override
    public Booking create(BookingRequestDto bookingDto) {
        Vehicle vehicle =  vehicleService.getById(bookingDto.getVehicleId());

        Booking booking = new Booking(
                vehicle, bookingDto.getBookedFromDate(), bookingDto.getBookedToDate(),
                BookingStatus.CREATED, bookingDto.getCityStart(), bookingDto.getCityEnd());

        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(Long id, BookingRequestDto bookingDto) {
        return null;
    }

    @Override
    public Booking getById(Long id) {
        return null;
    }

    @Override
    public List<Booking> getBookingsByStatus(BookingStatus bookingStatus) {
        return null;
    }

    @Override
    public List<Booking> getBookingsByUserId(Long userId) {
        return null;
    }
}
