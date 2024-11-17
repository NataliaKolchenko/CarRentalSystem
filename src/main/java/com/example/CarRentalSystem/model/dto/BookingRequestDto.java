package com.example.CarRentalSystem.model.dto;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.enums.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {
    private String userId;
    private Long vehicleId;
    private LocalDate bookedFromDate;
    private LocalDate bookedToDate;
    private City cityStart;
    private City cityEnd;
}
