package com.example.CarRentalSystem;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.exception.*;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Test
    public void testUpdate_ExistingBrand_Successfully(){
        Long existingBookingId = 1L;
        Long vehicleId = 1L;
        Long userId = 1L;
        BookingRequestDto requestDto = new BookingRequestDto(
                vehicleId,
                LocalDate.of(2024,1,12),
                LocalDate.of(2024,1,13),
                City.BONN,
                City.BONN);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        when(vehicleService.getById(requestDto.getVehicleId())).thenReturn(vehicle);

        Booking existingBooking = new Booking(
                vehicle,
                requestDto.getBookedFromDate(),
                requestDto.getBookedToDate(),
                BookingStatus.CREATED,
                requestDto.getCityStart(),
                requestDto.getCityEnd()
        );
        existingBooking.setId(existingBookingId);
        existingBooking.setUserId(userId);
        existingBooking.setCreateDate(LocalDateTime.now());

        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        Booking updated = new Booking(
                vehicle,
                requestDto.getBookedFromDate(),
                requestDto.getBookedToDate(),
                BookingStatus.CREATED,
                requestDto.getCityStart(),
                requestDto.getCityEnd()
        );
        updated.setId(existingBooking.getId());
        updated.setUserId(existingBooking.getUserId());
        updated.setCreateDate(existingBooking.getCreateDate());
        updated.setUpdateDate(LocalDateTime.now());

        when(bookingRepository.save(any(Booking.class))).thenReturn(updated);
        BookingResponseDto result = bookingService.update(existingBookingId, requestDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(updated.getVehicle().getId(), result.getVehicleId()),
                () -> assertEquals(updated.getBookedFromDate(), result.getBookedFromDate()),
                () -> assertEquals(updated.getBookedToDate(), result.getBookedToDate()),
                () -> assertEquals(updated.getStatus(), result.getStatus()),
                () -> assertEquals(updated.getCityStart(), result.getCityStart()),
                () -> assertEquals(updated.getCityEnd(), result.getCityEnd()),

                () -> verify(bookingRepository).save(any(Booking.class))
        );

    }

    @Test
    public void testUpdate_BookingIsAlreadyExist_ThrowsException(){
        Long existingBookingId = 1L;
        Long vehicleId = 1L;
        BookingRequestDto requestDto = new BookingRequestDto(
                vehicleId,
                LocalDate.of(2024,1,12),
                LocalDate.of(2024,1,13),
                City.BONN,
                City.BONN);
        List<Booking> existingBooking = List.of(new Booking());

        when(bookingRepository.checkExistingBooking(
                requestDto.getVehicleId(),
                requestDto.getBookedFromDate(),
                requestDto.getBookedToDate(),
                BookingStatus.FINISHED)).thenReturn(existingBooking);

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> bookingService.update(existingBookingId, requestDto));

        assertAll(
                () -> assertEquals("Booking with the same parameters is already exist", exception.getMessage()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

    @Test
    public void testUpdate_BookingCannotBeUpdated_ThrowsException(){
        Long existingBookingId = 1L;
        Long vehicleId = 1L;
        Long userId = 1L;
        BookingRequestDto requestDto = new BookingRequestDto(
                vehicleId,
                LocalDate.of(2024,1,12),
                LocalDate.of(2024,1,13),
                City.BONN,
                City.BONN);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        when(vehicleService.getById(requestDto.getVehicleId())).thenReturn(vehicle);

        Booking existingBooking = new Booking(
                vehicle,
                requestDto.getBookedFromDate(),
                requestDto.getBookedToDate(),
                BookingStatus.FINISHED,
                requestDto.getCityStart(),
                requestDto.getCityEnd()
        );
        existingBooking.setId(existingBookingId);
        existingBooking.setUserId(userId);
        existingBooking.setCreateDate(LocalDateTime.now());
        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        BookingCannotBeUpdatedException exception = assertThrows(BookingCannotBeUpdatedException.class,
                () -> bookingService.update(existingBookingId, requestDto));

        assertAll(
                () -> assertEquals("Booking can't be updated", exception.getMessage()),

                () -> verify(bookingRepository).findById(existingBookingId)
        );

    }

    @Test
    public void testGetById_Successfully(){
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);

        Long id = 1L;
        Booking expectedBooking = new Booking();
        expectedBooking.setId(id);
        expectedBooking.setUserId(1L);
        expectedBooking.setVehicle(vehicle);
        expectedBooking.setBookedFromDate(LocalDate.of(2024,1,12));
        expectedBooking.setBookedToDate(LocalDate.of(2024,1,13));
        expectedBooking.setCityStart(City.BONN);
        expectedBooking.setCityEnd(City.BONN);
        expectedBooking.setCreateDate(LocalDateTime.now());
        expectedBooking.setUpdateDate(null);

        when(bookingRepository.findById(id)).thenReturn(Optional.of(expectedBooking));

        BookingResponseDto bookingById = bookingService.getById(id);

        assertAll(
                () -> assertNotNull(bookingById),
                () -> assertEquals(expectedBooking.getId(), bookingById.getId()),
                () -> assertEquals(expectedBooking.getVehicle().getId(), bookingById.getVehicleId()),
                () -> assertEquals(expectedBooking.getCreateDate(), bookingById.getCreateDate()),
                () -> assertEquals(expectedBooking.getUpdateDate(), bookingById.getUpdateDate()),

                () -> verify(bookingRepository).findById(id)
        );
    }

    @Test
    public void testGetById_NotExistBookingId_ThrowsException(){
        Long bookingId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> bookingService.getById(bookingId));

        assertAll(
                () -> assertEquals("BookingId was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

     @Test
    public void testGetBookingsByStatus_FullList(){
        Long userId = 1L;
        BookingStatus bookingStatus = BookingStatus.CREATED;
        List<Booking> bookingList = new ArrayList<>();

        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setVehicle(vehicle);

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setVehicle(vehicle);

        bookingList.add(booking1);
        bookingList.add(booking2);

        when(bookingRepository.findByUserIdAndStatus(userId, bookingStatus)).thenReturn(bookingList);

        List<BookingResponseDto> actualList = bookingService.getBookingsByStatus(bookingStatus, userId);

        assertAll(
                () -> assertFalse(actualList.isEmpty()),
                () -> assertEquals(bookingList.size(), actualList.size()),

                () -> verify(bookingRepository).findByUserIdAndStatus(userId, bookingStatus)
        );

     }

    @Test
    public void testGetBookingsByStatus_EmptyList(){
        Long userId = 1L;
        BookingStatus bookingStatus = BookingStatus.CREATED;
        when(bookingRepository.findByUserIdAndStatus(userId, bookingStatus)).thenReturn(Collections.emptyList());

        List<BookingResponseDto> actualList = bookingService.getBookingsByStatus(bookingStatus, userId);

        assertAll(
                () -> assertEquals(Collections.emptyList(), actualList),
                () -> assertTrue(actualList.isEmpty()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

    @Test
    public void testGetBookingsByUserId_FullList(){
        Long userId = 1L;
        List<Booking> bookingList = new ArrayList<>();

        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setVehicle(vehicle);

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setVehicle(vehicle);

        bookingList.add(booking1);
        bookingList.add(booking2);

        when(bookingRepository.findByUserId(userId)).thenReturn(bookingList);

        List<BookingResponseDto> actualList = bookingService.getBookingsByUserId(userId);

        assertAll(
                () -> assertFalse(actualList.isEmpty()),
                () -> assertEquals(bookingList.size(), actualList.size()),

                () -> verify(bookingRepository).findByUserId(userId)
        );

    }

    @Test
    public void testGetBookingsByUserId_EmptyList(){
        Long userId = 1L;
        when(bookingRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<BookingResponseDto> actualList = bookingService.getBookingsByUserId(userId);

        assertAll(
                () -> assertEquals(Collections.emptyList(), actualList),
                () -> assertTrue(actualList.isEmpty()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

    @Test
    public void testCancel_Successfully(){
        Long existingBookingId = 1L;
        Long vehicleId = 1L;

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Booking existingBooking = new Booking();
        existingBooking.setId(existingBookingId);
        existingBooking.setVehicle(vehicle);
        existingBooking.setStatus(BookingStatus.CREATED);

        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        when(bookingRepository.save(any(Booking.class))).thenReturn(existingBooking);


        Boolean resultDto = bookingService.cancel(existingBookingId);

        assertAll(
                () -> assertTrue(resultDto),

                () -> verify(bookingRepository).findById(existingBookingId),
                () -> verify(bookingRepository).save(any())
        );
    }

    @Test
    public void testCancel_BookingCannotBeCancelled_ThrowsException(){
        Long existingBookingId = 1L;
        Long vehicleId = 1L;

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Booking existingBooking = new Booking();
        existingBooking.setId(existingBookingId);
        existingBooking.setVehicle(vehicle);
        existingBooking.setStatus(BookingStatus.ACTIVE);

        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        BookingCannotBeCancelledException exception = assertThrows(BookingCannotBeCancelledException.class,
                () -> bookingService.cancel(existingBookingId));

        assertAll(
                () -> assertEquals("Booking can't be cancelled", exception.getMessage()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

    @Test
    public void testActivate_Successfully(){
        Long existingBookingId = 1L;
        Long vehicleId = 1L;

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Booking existingBooking = new Booking();
        existingBooking.setId(existingBookingId);
        existingBooking.setVehicle(vehicle);
        existingBooking.setStatus(BookingStatus.CREATED);

        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        when(bookingRepository.save(any(Booking.class))).thenReturn(existingBooking);

        Boolean result = bookingService.activate(existingBookingId);


        assertAll(
                () -> assertTrue(result),

                () -> verify(bookingRepository).findById(existingBookingId),
                () -> verify(bookingRepository).save(any())
        );
    }

    @Test
    public void testActivate_BookingCannotBeActivated_ThrowsException(){
        Long existingBookingId = 1L;
        Long vehicleId = 1L;

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Booking existingBooking = new Booking();
        existingBooking.setId(existingBookingId);
        existingBooking.setVehicle(vehicle);
        existingBooking.setStatus(BookingStatus.ACTIVE);

        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        BookingCannotBeActivatedException exception = assertThrows(BookingCannotBeActivatedException.class,
                () -> bookingService.activate(existingBookingId));

        assertAll(
                () -> assertEquals("Booking can't be activated", exception.getMessage()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

    @Test
    public void testFinish_Successfully(){
        Long existingBookingId = 1L;
        Long vehicleId = 1L;

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Booking existingBooking = new Booking();
        existingBooking.setId(existingBookingId);
        existingBooking.setVehicle(vehicle);
        existingBooking.setStatus(BookingStatus.ACTIVE);
        existingBooking.setBookedToDate(LocalDate.now());

        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        when(bookingRepository.save(any(Booking.class))).thenReturn(existingBooking);

        Boolean result = bookingService.finish(existingBookingId);


        assertAll(
                () -> assertTrue(result),

                () -> verify(bookingRepository).findById(existingBookingId),
                () -> verify(bookingRepository).save(any())
        );
    }

    @Test
    public void testFinish_BookingCannotBeFinish_ThrowsException(){
        Long existingBookingId = 1L;
        Long vehicleId = 1L;

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Booking existingBooking = new Booking();
        existingBooking.setId(existingBookingId);
        existingBooking.setVehicle(vehicle);
        existingBooking.setStatus(BookingStatus.CREATED);

        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        BookingCannotBeFinishedException exception = assertThrows(BookingCannotBeFinishedException.class,
                () -> bookingService.finish(existingBookingId));

        assertAll(
                () -> assertEquals("Booking can't be finished", exception.getMessage()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

}
