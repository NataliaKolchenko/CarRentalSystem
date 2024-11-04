package com.example.CarRentalSystem;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.exception.SubjectNotFoundException;
import com.example.CarRentalSystem.model.Booking;
import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.model.dto.BookingResponseDto;
import com.example.CarRentalSystem.repository.JpaBookingRepository;
import com.example.CarRentalSystem.service.BookingServiceImp;
import com.example.CarRentalSystem.service.interfaces.BookingService;
import com.example.CarRentalSystem.service.interfaces.VehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImpTest {

    @InjectMocks
    private BookingServiceImp bookingService;

    @Mock
    private JpaBookingRepository bookingRepository;

    @Mock
    private VehicleService vehicleService;

    @Test
    public void testCreate_NewBooking_Successfully(){
        BookingRequestDto requestDto = new BookingRequestDto(
                1L,
                LocalDate.of(2024,1,12),
                LocalDate.of(2024,1,12),
                City.BERLIN,
                City.BERLIN);

        Vehicle vehicle = new Vehicle();
        when(vehicleService.getById(requestDto.getVehicleId())).thenReturn(vehicle);

        Booking booking = new Booking(
                vehicle,
                requestDto.getBookedFromDate(),
                requestDto.getBookedToDate(),
                BookingStatus.CREATED,
                requestDto.getCityStart(),
                requestDto.getCityEnd()
        );

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
       BookingResponseDto responseDto =  bookingService.create(requestDto);

        assertAll(
                () -> assertNotNull(responseDto),
                () -> assertEquals(booking.getVehicle().getId(), responseDto.getVehicleId()),
                () -> assertEquals(booking.getBookedFromDate(), responseDto.getBookedFromDate()),
                () -> assertEquals(booking.getBookedToDate(), responseDto.getBookedToDate()),
                () -> assertEquals(booking.getStatus(), responseDto.getStatus()),
                () -> assertEquals(booking.getCityStart(), responseDto.getCityStart()),
                () -> assertEquals(booking.getCityEnd(), responseDto.getCityEnd()),

                () -> verify(bookingRepository).save(any(Booking.class))
        );

    }

    @Test
    public void testCreate_BookingIsAlreadyExist_ThrowsException(){
        List<Booking> existingBooking = List.of(new Booking());

        BookingRequestDto requestDto = new BookingRequestDto(
                1L,
                LocalDate.of(2024,1,12),
                LocalDate.of(2024,1,12),
                City.BERLIN,
                City.BERLIN);

        when(bookingRepository.checkExistingBooking(
                requestDto.getVehicleId(),
                requestDto.getBookedFromDate(),
                requestDto.getBookedToDate(),
                BookingStatus.FINISHED)).thenReturn(existingBooking);

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> bookingService.create(requestDto));

        assertAll(
                () -> assertEquals("Booking with the same parameters is already exist", exception.getMessage()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }
}
