package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.model.dto.BookingResponseDto;
import com.example.CarRentalSystem.service.interfaces.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/booking")
@Tag(name = "Booking Controller", description = "Booking Controller is available only to users with the \"USER\" role")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "Create a new booking")
    @PostMapping("/createBooking")
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody @Valid BookingRequestDto bookingDto) {
        bookingDto.setUserId(getUserId());
        return ResponseEntity.ok(bookingService.create(bookingDto));
    }

    @Operation(summary = "Get full information by ID about booking",
            description = " The user can only receive information about the booking that he has booked himself")
    @GetMapping("/getBookingById/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getById(id, getUserId()));
    }

    @Operation(summary = "Get all booking list by UserID",
            description = "The user can get a list of bookings that he has made himself")
    @GetMapping("/getBookingListByUserId")
    public ResponseEntity<List<BookingResponseDto>> getBookingListByUserId() {
        return ResponseEntity.ok(bookingService.getBookingsByUserId(getUserId()));
    }

    @Operation(summary = "Get all booking list by UserID and certain booking status",
            description = "The user can get a list of bookings that he has made himself")
    @GetMapping("/getBookingListByStatus/{status}")
    public ResponseEntity<List<BookingResponseDto>> getBookingByStatus(@PathVariable BookingStatus status) {
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status, getUserId()));
    }

    @Operation(summary = "Update a booking by UserID",
            description = "The user can update booking that he has made himself")
    @PutMapping("/updateBooking/{id}")
    public ResponseEntity<BookingResponseDto> updateBooking(@PathVariable Long id,
                                                            @RequestBody @Valid BookingRequestDto bookingRequestDto) {
        bookingRequestDto.setUserId(getUserId());
        return ResponseEntity.ok(bookingService.update(id, bookingRequestDto));
    }

    @Operation(summary = "Cancel booking",
            description = "The user can cancel booking that he has made himself")
    @PutMapping("/cancelBooking")
    public ResponseEntity<Boolean> cancelBooking(@RequestBody @Valid Long id) {
        return ResponseEntity.ok(bookingService.cancel(id, getUserId()));
    }

    @Operation(summary = "Activate booking",
            description = "The user can activate booking that he has made himself")
    @PutMapping("/activateBooking")
    public ResponseEntity<Boolean> activateBooking(@RequestBody @Valid Long id) {
        return ResponseEntity.ok(bookingService.activate(id, getUserId()));
    }

    @Operation(summary = "Finish booking",
            description = "The user can finish booking that he has made himself")

    @PutMapping("/finishBooking")
    public ResponseEntity<Boolean> finishBooking(@RequestBody @Valid Long id) {
        return ResponseEntity.ok(bookingService.finish(id, getUserId()));
    }

    private static String getUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
