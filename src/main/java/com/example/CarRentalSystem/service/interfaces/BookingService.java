package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.model.dto.BookingResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BookingService {
    BookingResponseDto create(HttpServletRequest request, BookingRequestDto bookingDto);
    BookingResponseDto update (Long id, BookingRequestDto bookingDto);
    BookingResponseDto getById (Long id);
    List<BookingResponseDto> getBookingsByStatus(BookingStatus bookingStatus, Long userId);
    List<BookingResponseDto> getBookingsByUserId(Long userId);
    Boolean cancel(Long id);
    Boolean activate(Long id);
    Boolean finish(Long id);

}
