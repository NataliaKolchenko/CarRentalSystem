package com.example.CarRentalSystem.model.dto;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.enums.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingResponseDto {
    private Long id;
    private Long userId;
    private Long vehicleId;
    private LocalDate bookedFromDate;
    private LocalDate bookedToDate;
    private BookingStatus status;
    private City cityStart;
    private City cityEnd;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

}
