package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.exception.*;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.infrastructure.JwtAuthFilter;
import com.example.CarRentalSystem.model.Booking;
import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.model.dto.BookingResponseDto;
import com.example.CarRentalSystem.model.dto.VehicleRequestDto;
import com.example.CarRentalSystem.repository.JpaBookingRepository;
import com.example.CarRentalSystem.service.auth.JwtService;
import com.example.CarRentalSystem.service.interfaces.BookingService;
import com.example.CarRentalSystem.service.interfaces.VehicleService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImp implements BookingService {
    private final JpaBookingRepository bookingRepository;
    private final VehicleService vehicleService;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtService tokenValidator;

    public BookingServiceImp(JpaBookingRepository bookingRepository, VehicleService vehicleService, JwtAuthFilter jwtAuthFilter, JwtService tokenValidator) {
        this.bookingRepository = bookingRepository;
        this.vehicleService = vehicleService;
        this.jwtAuthFilter = jwtAuthFilter;
        this.tokenValidator = tokenValidator;
    }

    @Override
    public BookingResponseDto create(BookingRequestDto bookingDto) {

        checkBookingParameters(bookingDto);

        Vehicle vehicle = vehicleService.getById(bookingDto.getVehicleId());

        Booking booking = new Booking(
                bookingDto.getUserId(),
                vehicle,
                bookingDto.getBookedFromDate(),
                bookingDto.getBookedToDate(),
                BookingStatus.CREATED,
                bookingDto.getCityStart(),
                bookingDto.getCityEnd());

        Booking savedBooking = bookingRepository.save(booking);

        return mapEntityToDto(savedBooking);
    }

    @Override
    public BookingResponseDto update(Long bookingId, BookingRequestDto bookingDto) {
        checkBookingParameters(bookingDto);

        BookingResponseDto existingBookingDto = getById(bookingId, bookingDto.getUserId());
        Booking existingBooking = mapDtoToEntity(existingBookingDto);

        switch (existingBooking.getStatus()){
            case FINISHED, CANCELLED, ACTIVE -> throw  new BookingCannotBeUpdatedException(
                    ErrorMessage.BOOKING_CANNOT_BE_UPDATED);
        }

        Vehicle vehicle = vehicleService.getById(bookingDto.getVehicleId());

        existingBooking.setVehicle(vehicle);
        existingBooking.setBookedFromDate(bookingDto.getBookedFromDate());
        existingBooking.setBookedToDate(bookingDto.getBookedToDate());
        existingBooking.setCityStart(bookingDto.getCityStart());
        existingBooking.setCityEnd(bookingDto.getCityEnd());
        existingBooking.setUpdateDate(LocalDateTime.now());

        Booking saved = bookingRepository.save(existingBooking);

        return mapEntityToDto(saved);
    }




    @Override
    public BookingResponseDto getById(Long bookingId, String userId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        Booking booking = bookingOpt.orElseThrow(
                () -> new SubjectNotFoundException(ErrorMessage.BOOKING_ID_WAS_NOT_FOUND));
        checkMatchUserId(booking, userId);

        return mapEntityToDto(booking);
    }

    @Override
    public List<BookingResponseDto> getBookingsByStatus(BookingStatus bookingStatus, String userId) {
        List<Booking> bookingList = bookingRepository.findByUserIdAndStatus(userId, bookingStatus);
        return bookingList.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getBookingsByUserId(String userId) {
        List<Booking> bookingListByUserId = bookingRepository.findByUserId(userId);

        return bookingListByUserId.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean cancel(Long bookingId, String userId) {
        BookingResponseDto existingBookingDto = getById(bookingId, userId);
        Booking existingBooking = mapDtoToEntity(existingBookingDto);

        switch(existingBooking.getStatus()){
            case ACTIVE, FINISHED, CANCELLED -> throw  new BookingCannotBeCancelledException(
                    ErrorMessage.BOOKING_CANNOT_BE_CANCELLED);
        }
        existingBooking.setStatus(BookingStatus.CANCELLED);
        existingBooking.setUpdateDate(LocalDateTime.now());
        bookingRepository.save(existingBooking);


        return true;
    }

    @Override
    public Boolean activate(Long bookingId, String userId) {
        BookingResponseDto existingBookingDto = getById(bookingId, userId);
        Booking existingBooking = mapDtoToEntity(existingBookingDto);
        switch (existingBooking.getStatus()){
            case ACTIVE, CANCELLED, WAITING_PAYMENT, PAYED, FINISHED ->  throw new BookingCannotBeActivatedException(
                    ErrorMessage.BOOKING_CANNOT_BE_ACTIVATED + " due to an unsuitable booking status");
        }

        if (!LocalDate.now().equals(existingBookingDto.getBookedFromDate())){
            throw new BookingCannotBeActivatedException(
                    ErrorMessage.BOOKING_CANNOT_BE_ACTIVATED + " due to an incorrect activation date");
        }
        existingBooking.setStatus(BookingStatus.ACTIVE);
        existingBooking.setUpdateDate(LocalDateTime.now());
        bookingRepository.save(existingBooking);

        return true;
    }

    @Override
    public Boolean finish(Long id) {
        BookingResponseDto existingBookingDto = getById(id, "user");
        Booking existingBooking = mapDtoToEntity(existingBookingDto);
        switch (existingBooking.getStatus()){
            case CREATED, CANCELLED, WAITING_PAYMENT, PAYED, FINISHED -> throw new BookingCannotBeFinishedException(
                    ErrorMessage.BOOKING_CANNOT_BE_FINISHED + " due to an unsuitable booking status");
        }

        if (!LocalDate.now().equals(existingBookingDto.getBookedToDate())){
            throw new BookingCannotBeFinishedException(
                    ErrorMessage.BOOKING_CANNOT_BE_FINISHED + " due to the incorrect date of the operation");
        }
        existingBooking.setStatus(BookingStatus.FINISHED);
        existingBooking.setUpdateDate(LocalDateTime.now());
        bookingRepository.save(existingBooking);

        Vehicle existingVehicle = vehicleService.getById(existingBookingDto.getVehicleId());

        VehicleRequestDto vehicleRequestDto = vehicleService.mapEntityToDto(existingVehicle);
        vehicleRequestDto.setCity(existingBooking.getCityEnd());

        vehicleService.update(existingBookingDto.getVehicleId(), vehicleRequestDto);
        return true;
    }

    private static void checkMatchUserId(Booking existingBooking, String userId) {
        if (!existingBooking.getUserId().equals(userId)) {
            throw new UserIdMismatchException(ErrorMessage.USER_ID_MISMATCH);
        }
    }

    private void checkBookingParameters(BookingRequestDto bookingDto) {
        List<Booking> existingBookings = bookingRepository.checkExistingBooking(
                bookingDto.getVehicleId(),
                bookingDto.getBookedFromDate(),
                bookingDto.getBookedToDate());

        if (!existingBookings.isEmpty()) {
            throw new SubjectNotFoundException((ErrorMessage.BOOKING_IS_ALREADY_EXIST));
        }
    }
    public BookingResponseDto mapEntityToDto(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getUserId(),
                booking.getVehicle().getId(),
                booking.getBookedFromDate(),
                booking.getBookedToDate(),
                booking.getStatus(),
                booking.getCityStart(),
                booking.getCityEnd(),
                booking.getCreateDate(),
                booking.getUpdateDate());
    }

    public Booking mapDtoToEntity(BookingResponseDto bookingResponseDto) {
        Vehicle vehicle = vehicleService.getById(bookingResponseDto.getVehicleId());

        Booking booking = new Booking();
        booking.setId(bookingResponseDto.getId());
        booking.setUserId(bookingResponseDto.getUserId());
        booking.setVehicle(vehicle);
        booking.setBookedFromDate(bookingResponseDto.getBookedFromDate());
        booking.setBookedToDate(bookingResponseDto.getBookedToDate());
        booking.setStatus(bookingResponseDto.getStatus());
        booking.setCityStart(bookingResponseDto.getCityStart());
        booking.setCityEnd(bookingResponseDto.getCityEnd());
        booking.setCreateDate(bookingResponseDto.getCreateDate());
        booking.setUpdateDate(bookingResponseDto.getUpdateDate());

        return booking;
    }

    
}
