package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.model.Booking;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.model.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto create(BookingRequestDto bookingDto);
    Booking update (Long id, BookingRequestDto bookingDto);
    Booking getById (Long id);
    List<Booking> getBookingsByStatus(BookingStatus bookingStatus);
    List<Booking> getBookingsByUserId(Long userId);
}
