package com.example.CarRentalSystem;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.exception.*;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.Booking;
import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.model.dto.BookingResponseDto;
import com.example.CarRentalSystem.model.dto.VehicleRequestDto;
import com.example.CarRentalSystem.repository.JpaBookingRepository;
import com.example.CarRentalSystem.service.BookingServiceImp;
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
    public void testCreate_NewBooking_Successfully() {
        BookingRequestDto requestDto = new BookingRequestDto(
                "userID",
                1L,
                LocalDate.of(2024, 1, 12),
                LocalDate.of(2024, 1, 12),
                City.BERLIN,
                City.BERLIN);

        Vehicle vehicle = new Vehicle();
        when(vehicleService.getById(requestDto.getVehicleId())).thenReturn(vehicle);

        Booking booking = new Booking(
                "userID",
                vehicle,
                requestDto.getBookedFromDate(),
                requestDto.getBookedToDate(),
                BookingStatus.CREATED,
                requestDto.getCityStart(),
                requestDto.getCityEnd()
        );

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        BookingResponseDto responseDto = bookingService.create(requestDto);

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
    public void testCreate_BookingIsAlreadyExist_ThrowsException() {
        List<Booking> existingBooking = List.of(new Booking());

        BookingRequestDto requestDto = new BookingRequestDto(
                "userID",
                1L,
                LocalDate.of(2024, 1, 12),
                LocalDate.of(2024, 1, 12),
                City.BERLIN,
                City.BERLIN);

        when(bookingRepository.checkExistingBooking(
                requestDto.getVehicleId(),
                requestDto.getBookedFromDate(),
                requestDto.getBookedToDate())).thenReturn(existingBooking);

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> bookingService.create(requestDto));

        assertAll(
                () -> assertEquals("Booking with the same parameters is already exist", exception.getMessage()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

    @Test
    public void testUpdate_ExistingBrand_Successfully() {
        Long existingBookingId = 1L;
        Long vehicleId = 1L;
        String userId = "userId";
        BookingRequestDto requestDto = new BookingRequestDto(
                userId,
                vehicleId,
                LocalDate.of(2024, 1, 12),
                LocalDate.of(2024, 1, 13),
                City.BONN,
                City.BONN);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        when(vehicleService.getById(requestDto.getVehicleId())).thenReturn(vehicle);

        Booking existingBooking = new Booking(
                userId,
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
                userId,
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
    public void testUpdate_BookingIsAlreadyExist_ThrowsException() {
        Long existingBookingId = 1L;
        Long vehicleId = 1L;
        BookingRequestDto requestDto = new BookingRequestDto(
                "userId",
                vehicleId,
                LocalDate.of(2024, 1, 12),
                LocalDate.of(2024, 1, 13),
                City.BONN,
                City.BONN);
        List<Booking> existingBooking = List.of(new Booking());

        when(bookingRepository.checkExistingBooking(
                requestDto.getVehicleId(),
                requestDto.getBookedFromDate(),
                requestDto.getBookedToDate())).thenReturn(existingBooking);

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> bookingService.update(existingBookingId, requestDto));

        assertAll(
                () -> assertEquals("Booking with the same parameters is already exist", exception.getMessage()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

    @Test
    public void testUpdate_BookingCannotBeUpdated_ThrowsException() {
        Long existingBookingId = 1L;
        Long vehicleId = 1L;
        String userId = "userId";
        BookingRequestDto requestDto = new BookingRequestDto(
                userId,
                vehicleId,
                LocalDate.of(2024, 1, 12),
                LocalDate.of(2024, 1, 13),
                City.BONN,
                City.BONN);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        when(vehicleService.getById(requestDto.getVehicleId())).thenReturn(vehicle);

        Booking existingBooking = new Booking(
                userId,
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
    public void testGetById_Successfully() {
        String userId = "userId";
        Long vehicleId = 1L;
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Long bookingId = 1L;
        Booking expectedBooking = new Booking();
        expectedBooking.setId(bookingId);
        expectedBooking.setUserId(userId);
        expectedBooking.setVehicle(vehicle);
        expectedBooking.setBookedFromDate(LocalDate.of(2024, 1, 12));
        expectedBooking.setBookedToDate(LocalDate.of(2024, 1, 13));
        expectedBooking.setCityStart(City.BONN);
        expectedBooking.setCityEnd(City.BONN);
        expectedBooking.setCreateDate(LocalDateTime.now());
        expectedBooking.setUpdateDate(null);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));


        BookingResponseDto bookingById = bookingService.getById(bookingId, userId);

        assertAll(
                () -> assertNotNull(bookingById),
                () -> assertEquals(expectedBooking.getId(), bookingById.getId()),
                () -> assertEquals(expectedBooking.getVehicle().getId(), bookingById.getVehicleId()),
                () -> assertEquals(expectedBooking.getCreateDate(), bookingById.getCreateDate()),
                () -> assertEquals(expectedBooking.getUpdateDate(), bookingById.getUpdateDate()),

                () -> verify(bookingRepository).findById(bookingId)
        );
    }

    @Test
    public void testGetById_NotExistBookingId_ThrowsException() {
        Long bookingId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class,
                () -> bookingService.getById(bookingId,"userId"));

        assertAll(
                () -> assertEquals("BookingId was not found", exception.getMessage()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

    @Test
    public void testGetById_UserIdMismatch_ThrowsException(){
        Long bookingId = 1L;
        String userId = "userId";

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setUserId(userId);

        booking.setUserId("differentUserId");
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        UserIdMismatchException exception = assertThrows(UserIdMismatchException.class,
                () -> bookingService.getById(bookingId, userId)
        );

        assertAll(
                () -> assertEquals(ErrorMessage.USER_ID_MISMATCH, exception.getMessage()),
                () -> verify(bookingRepository).findById(bookingId)
        );

    }

    @Test
    public void testGetBookingsByStatus_FullList() {
        String userId = "userId";
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
    public void testGetBookingsByStatus_EmptyList() {
        String userId = "userId";
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
    public void testGetBookingsByUserId_FullList() {
        String userId = "userId";
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
    public void testGetBookingsByUserId_EmptyList() {
        String userId = "userId";
        when(bookingRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<BookingResponseDto> actualList = bookingService.getBookingsByUserId(userId);

        assertAll(
                () -> assertEquals(Collections.emptyList(), actualList),
                () -> assertTrue(actualList.isEmpty()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

    @Test
    public void testCancel_Successfully() {
        String userId = "userId";
        Long existingBookingId = 1L;
        Long vehicleId = 1L;

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Booking existingBooking = new Booking();
        existingBooking.setId(existingBookingId);
        existingBooking.setVehicle(vehicle);
        existingBooking.setStatus(BookingStatus.CREATED);
        existingBooking.setUserId(userId);

        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        Booking canceledBooking = new Booking();
        canceledBooking.setId(existingBookingId);
        canceledBooking.setVehicle(vehicle);
        canceledBooking.setStatus(BookingStatus.CANCELLED);
        canceledBooking.setUpdateDate(LocalDateTime.now());
        canceledBooking.setUserId(userId);

        when(bookingRepository.save(any(Booking.class))).thenReturn(canceledBooking);

        Boolean resultDto = bookingService.cancel(existingBookingId, userId);

        assertAll(
                () -> assertTrue(resultDto),
                () -> assertEquals(BookingStatus.CANCELLED, canceledBooking.getStatus()),
                () -> assertNotNull(canceledBooking.getUpdateDate()),

                () -> verify(bookingRepository).findById(existingBookingId),
                () -> verify(bookingRepository).save(any())
        );
    }

    @Test
    public void testCancel_BookingCannotBeCancelled_ThrowsException() {
        String userId = "userId";
        Long existingBookingId = 1L;
        Long vehicleId = 1L;

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Booking existingBooking = new Booking();
        existingBooking.setId(existingBookingId);
        existingBooking.setVehicle(vehicle);
        existingBooking.setStatus(BookingStatus.ACTIVE);
        existingBooking.setUserId(userId);

        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        BookingCannotBeCancelledException exception = assertThrows(BookingCannotBeCancelledException.class,
                () -> bookingService.cancel(existingBookingId, userId));

        assertAll(
                () -> assertEquals("Booking can't be cancelled", exception.getMessage()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

    @Test
    public void testActivate_Successfully() {
        String userId = "userId";
        Long existingBookingId = 1L;
        Long vehicleId = 1L;

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Booking existingBooking = new Booking();
        existingBooking.setId(existingBookingId);
        existingBooking.setVehicle(vehicle);
        existingBooking.setStatus(BookingStatus.CREATED);
        existingBooking.setBookedFromDate(LocalDate.now());
        existingBooking.setUserId(userId);

        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        Booking activatedBooking = new Booking();
        activatedBooking.setId(existingBookingId);
        activatedBooking.setVehicle(vehicle);
        activatedBooking.setStatus(BookingStatus.ACTIVE);
        activatedBooking.setUpdateDate(LocalDateTime.now());
        activatedBooking.setUserId(userId);

        when(bookingRepository.save(any(Booking.class))).thenReturn(activatedBooking);

        Boolean result = bookingService.activate(existingBookingId, userId);

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(BookingStatus.ACTIVE, activatedBooking.getStatus()),
                () -> assertNotNull(activatedBooking.getUpdateDate()),

                () -> verify(bookingRepository).findById(existingBookingId),
                () -> verify(bookingRepository).save(any())
        );
    }

    @Test
    public void testActivate_BookingCannotBeActivatedBecauseStatus_ThrowsException() {
        String userId = "userId";
        Long existingBookingId = 1L;
        Long vehicleId = 1L;

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Booking existingBooking = new Booking();
        existingBooking.setId(existingBookingId);
        existingBooking.setVehicle(vehicle);
        existingBooking.setStatus(BookingStatus.ACTIVE);
        existingBooking.setUserId(userId);

        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        BookingCannotBeActivatedException exception = assertThrows(BookingCannotBeActivatedException.class,
                () -> bookingService.activate(existingBookingId, userId));

        assertAll(
                () -> assertEquals("Booking can't be activated due to an unsuitable booking status", exception.getMessage()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

    @Test
    public void testActivate_BookingCannotBeActivatedBecauseDate_ThrowsException() {
        Long existingBookingId = 1L;
        Long vehicleId = 1L;
        String userId = "userId";

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Booking existingBooking = new Booking();
        existingBooking.setId(existingBookingId);
        existingBooking.setVehicle(vehicle);
        existingBooking.setStatus(BookingStatus.CREATED);
        existingBooking.setBookedFromDate(LocalDate.parse("2024-09-09"));
        existingBooking.setUserId(userId);

        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        BookingCannotBeActivatedException exception = assertThrows(BookingCannotBeActivatedException.class,
                () -> bookingService.activate(existingBookingId, userId));

        assertAll(
                () -> assertEquals("Booking can't be activated due to an incorrect activation date", exception.getMessage()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

    @Test
    public void testFinish_Successfully() {
        String userId = "userId";
        Long existingBookingId = 1L;
        Long vehicleId = 1L;

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        vehicle.setCity(City.BONN);

        Booking existingBooking = new Booking();
        existingBooking.setId(existingBookingId);
        existingBooking.setVehicle(vehicle);
        existingBooking.setStatus(BookingStatus.ACTIVE);
        existingBooking.setCityEnd(City.BERLIN);
        existingBooking.setBookedToDate(LocalDate.now());
        existingBooking.setUserId(userId);

        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        Booking finishedBooking = new Booking();
        finishedBooking.setId(existingBookingId);
        finishedBooking.setVehicle(vehicle);
        finishedBooking.setStatus(BookingStatus.FINISHED);
        finishedBooking.setUpdateDate(LocalDateTime.now());
        finishedBooking.setCityEnd(existingBooking.getCityEnd());
        finishedBooking.setUserId(userId);

        when(bookingRepository.save(any(Booking.class))).thenReturn(existingBooking);

        when(vehicleService.getById(vehicleId)).thenReturn(vehicle);

        VehicleRequestDto vehicleRequestDto = new VehicleRequestDto();
        vehicleRequestDto.setCity(existingBooking.getCityEnd());

        when(vehicleService.mapEntityToDto(vehicle)).thenReturn(vehicleRequestDto);

        Vehicle updatedVehicle = new Vehicle();
        updatedVehicle.setId(vehicleId);
        updatedVehicle.setCity(existingBooking.getCityEnd());
        updatedVehicle.setUpdateDate(LocalDateTime.now());
        updatedVehicle.setCity(existingBooking.getCityEnd());

        when(vehicleService.update(eq(vehicleId), any(VehicleRequestDto.class))).thenReturn(updatedVehicle);
        Boolean result = bookingService.finish(existingBookingId, userId);

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(BookingStatus.FINISHED, finishedBooking.getStatus()),
                () -> assertNotNull(finishedBooking.getUpdateDate()),
                () -> assertEquals(finishedBooking.getCityEnd(), updatedVehicle.getCity()),
                () -> assertNotNull(updatedVehicle.getUpdateDate()),

                () -> verify(bookingRepository).findById(existingBookingId),
                () -> verify(bookingRepository).save(any(Booking.class)),
                () -> verify(vehicleService).update(eq(vehicleId), any(VehicleRequestDto.class))

        );
    }

    @Test
    public void testFinish_BookingCannotBeFinish_ThrowsException() {
        String userId = "userId";
        Long existingBookingId = 1L;
        Long vehicleId = 1L;

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Booking existingBooking = new Booking();
        existingBooking.setId(existingBookingId);
        existingBooking.setVehicle(vehicle);
        existingBooking.setStatus(BookingStatus.CREATED);
        existingBooking.setUserId(userId);

        when(bookingRepository.findById(existingBookingId)).thenReturn(Optional.of(existingBooking));

        BookingCannotBeFinishedException exception = assertThrows(BookingCannotBeFinishedException.class,
                () -> bookingService.finish(existingBookingId, userId));

        assertAll(
                () -> assertEquals("Booking can't be finished due to an unsuitable booking status", exception.getMessage()),

                () -> verifyNoMoreInteractions(bookingRepository)
        );
    }

}
