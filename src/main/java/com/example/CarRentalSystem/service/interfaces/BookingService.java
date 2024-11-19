package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.model.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto create(BookingRequestDto bookingDto);
    BookingResponseDto update (Long bookingId, BookingRequestDto bookingDto);
    BookingResponseDto getById (Long bookingId, String userId);
    List<BookingResponseDto> getBookingsByStatus(BookingStatus bookingStatus, String userId);
    List<BookingResponseDto> getBookingsByUserId(String userId);
    Boolean cancel(Long bookingId, String userId);
    Boolean activate( Long bookingId, String userId);
    Boolean finish(Long bookingId, String userId);

}
