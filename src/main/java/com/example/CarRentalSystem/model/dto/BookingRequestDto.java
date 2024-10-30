package com.example.CarRentalSystem.model.dto;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.enums.City;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingRequestDto {
    private Long vehicleId;
    private LocalDate bookedFromDate;
    private LocalDate bookedToDate;
    private BookingStatus status;
    private City cityStart;
    private City cityEnd;
}
