package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.Booking;
import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.model.dto.BookingResponseDto;
import com.example.CarRentalSystem.repository.JpaBookingRepository;
import com.example.CarRentalSystem.service.interfaces.BookingService;
import com.example.CarRentalSystem.service.interfaces.VehicleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public BookingResponseDto create(BookingRequestDto bookingDto) {
        BookingStatus status = BookingStatus.FINISHED;

        List<Booking> existingBookings = bookingRepository.checkExistingBooking(
                bookingDto.getVehicleId(), bookingDto.getBookedFromDate(), bookingDto.getBookedToDate(), status);

        if (!existingBookings.isEmpty()) {
            throw new SubjectNotFoundException((ErrorMessage.BOOKING_IS_ALREADY_EXIST));
        }

        Vehicle vehicle = vehicleService.getById(bookingDto.getVehicleId());

        Booking booking = new Booking(
                vehicle, bookingDto.getBookedFromDate(), bookingDto.getBookedToDate(),
                BookingStatus.CREATED, bookingDto.getCityStart(), bookingDto.getCityEnd());

        Booking savedBooking = bookingRepository.save(booking);

        BookingResponseDto dtoResponse = new BookingResponseDto(
                savedBooking.getId(),
                savedBooking.getUserId(),
                savedBooking.getVehicle().getId(),
                savedBooking.getBookedFromDate(),
                savedBooking.getBookedToDate(),
                savedBooking.getStatus(),
                savedBooking.getCityStart(),
                savedBooking.getCityEnd(),
                savedBooking.getCreateDate(),
                savedBooking.getUpdateDate());
        return dtoResponse;
    }

    @Override
    public BookingResponseDto update(Long id, BookingRequestDto bookingDto) {
        return null;
    }

    @Override
    public BookingResponseDto getById(Long id) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        Booking booking = bookingOpt.orElseThrow(() -> new SubjectNotFoundException(ErrorMessage.BOOKING_ID_WAS_NOT_FOUND));

        return mapEntityToDto(booking);
    }

    @Override
    public List<BookingResponseDto> getBookingsByStatus(BookingStatus bookingStatus, Long userId) {
        List<Booking> bookingList = bookingRepository.findByUserIdAndStatus(userId, bookingStatus);
        return bookingList.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<BookingResponseDto> getBookingsByUserId(Long userId) {
        List<Booking> bookingListByUserId = bookingRepository.findByUserId(userId);

        return bookingListByUserId.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getFavoriteBooking() {
        return null;
    }

    public BookingResponseDto mapEntityToDto(Booking booking) {
        BookingResponseDto bookingResponse = new BookingResponseDto(
                booking.getId(), booking.getUserId(), booking.getVehicle().getId(), booking.getBookedFromDate(), booking.getBookedToDate(),
                booking.getStatus(), booking.getCityStart(), booking.getCityEnd(), booking.getCreateDate(),
                booking.getUpdateDate());
        return bookingResponse;
    }
}
