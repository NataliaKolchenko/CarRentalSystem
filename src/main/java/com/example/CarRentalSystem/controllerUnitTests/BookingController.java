package com.example.CarRentalSystem.controllerUnitTests;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.model.dto.BookingResponseDto;
import com.example.CarRentalSystem.service.interfaces.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/getBookingListByStatus/{userId}/{status}")
    public ResponseEntity<List<BookingResponseDto>> getBookingByStatus(@PathVariable Long userId,
                                                                       @PathVariable BookingStatus status){
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status, userId));
    }

    @PutMapping("/updateBooking/{id}")
    public ResponseEntity<BookingResponseDto> updateBooking(@PathVariable Long id,
                                                            @RequestBody @Valid BookingRequestDto bookingRequestDto){
        return ResponseEntity.ok(bookingService.update(id, bookingRequestDto));
    }

    @PutMapping("/cancelBooking")
    public ResponseEntity<BookingResponseDto> cancelBooking(@RequestBody @Valid Long id){
        return ResponseEntity.ok(bookingService.cancel(id));
    }

    @PutMapping("/activateBooking")
    public ResponseEntity<Boolean> activateBooking(@RequestBody @Valid Long id){
        return ResponseEntity.ok(bookingService.activate(id));
    }

    @PutMapping("/finishBooking")
    public ResponseEntity<Boolean> finishBooking(@RequestBody @Valid Long id){
        return ResponseEntity.ok(bookingService.finish(id));
    }
}
