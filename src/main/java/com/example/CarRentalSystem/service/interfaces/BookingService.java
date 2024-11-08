package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.model.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto create(BookingRequestDto bookingDto);
    BookingResponseDto update (Long id, BookingRequestDto bookingDto);
    BookingResponseDto getById (Long id);
    List<BookingResponseDto> getBookingsByStatus(BookingStatus bookingStatus, Long userId);
    List<BookingResponseDto> getBookingsByUserId(Long userId);
    Boolean cancel(Long id);
    Boolean activate(Long id);
    Boolean finish(Long id);

}
