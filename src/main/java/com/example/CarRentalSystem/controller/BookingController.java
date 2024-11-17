package com.example.CarRentalSystem.controller;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.model.dto.BookingResponseDto;
import com.example.CarRentalSystem.service.interfaces.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/createBooking")
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody @Valid BookingRequestDto bookingDto){
        bookingDto.setUserId(getUserId());
        return ResponseEntity.ok(bookingService.create(bookingDto));
    }

    @GetMapping("/getBookingById/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getById(id, getUserId()));
    }

    @GetMapping("/getBookingListByUserId")
    public ResponseEntity<List<BookingResponseDto>> getBookingListByUserId(){
        return ResponseEntity.ok(bookingService.getBookingsByUserId(getUserId()));
    }

    @GetMapping("/getBookingListByStatus/{status}")
    public ResponseEntity<List<BookingResponseDto>> getBookingByStatus(@PathVariable BookingStatus status){
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status, getUserId()));
    }

    @PutMapping("/updateBooking/{id}")
    public ResponseEntity<BookingResponseDto> updateBooking(@PathVariable Long id,
                                                            @RequestBody @Valid BookingRequestDto bookingRequestDto){
        bookingRequestDto.setUserId(getUserId());
        return ResponseEntity.ok(bookingService.update(id, bookingRequestDto));
    }

    @PutMapping("/cancelBooking")
    public ResponseEntity<Boolean> cancelBooking(@RequestBody @Valid Long id){
        return ResponseEntity.ok(bookingService.cancel(id, getUserId()));
    }

    @PutMapping("/activateBooking")
    public ResponseEntity<Boolean> activateBooking(@RequestBody @Valid Long id){
        return ResponseEntity.ok(bookingService.activate(id, getUserId()));
    }

    @PutMapping("/finishBooking")
    public ResponseEntity<Boolean> finishBooking(@RequestBody @Valid Long id){
        return ResponseEntity.ok(bookingService.finish(id));
    }

    private static String getUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
