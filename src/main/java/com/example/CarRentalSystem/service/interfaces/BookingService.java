package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.model.dto.BookingResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BookingService {
    BookingResponseDto create(BookingRequestDto bookingDto);
    BookingResponseDto update (HttpServletRequest request, Long id, BookingRequestDto bookingDto);
    BookingResponseDto getById (HttpServletRequest request, Long id);
    List<BookingResponseDto> getBookingsByStatus(BookingStatus bookingStatus, Long userId);
    List<BookingResponseDto> getBookingsByUserId(Long userId);
    Boolean cancel(HttpServletRequest request, Long id);
    Boolean activate(HttpServletRequest request, Long id);
    Boolean finish(HttpServletRequest request, Long id);

}
