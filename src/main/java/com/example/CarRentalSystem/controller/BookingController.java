package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.model.Booking;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.model.dto.BookingResponseDto;
import com.example.CarRentalSystem.service.interfaces.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/createBooking")
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody @Valid BookingRequestDto bookingDto){
        return ResponseEntity.ok(bookingService.create(bookingDto));
    }

    @GetMapping("/getBookingById/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getById(id));
    }

    @GetMapping("/getBookingListByUserId/{id}")
    public ResponseEntity<List<BookingResponseDto>> getBookingListByUserId(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getBookingsByUserId(id));
    }

//
}
