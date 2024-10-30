package com.example.CarRentalSystem.model;

import com.example.CarRentalSystem.enums.BookingStatus;
import com.example.CarRentalSystem.enums.City;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    @NotNull(message = "vehicle may not be null")
    private Vehicle vehicle;

    @NotNull(message = "DateFrom may not be null")
    private LocalDate bookedFromDate;

    @NotNull(message = "DateTo may not be null")
    private LocalDate bookedToDate;

    @OneToOne(fetch = FetchType.EAGER)
    @NotNull(message = "BookingStatus may not be null")
    private BookingStatus status;

    @OneToOne(fetch = FetchType.EAGER)
    @NotNull(message = "cityStart may not be null")
    private City cityStart;

    @OneToOne(fetch = FetchType.EAGER)
    @NotNull(message = "cityEnd may not be null")
    private City cityEnd;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public Booking(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Booking(Vehicle vehicle, LocalDate bookedFromDate, LocalDate bookedToDate, City cityStart, City cityEnd) {
        this.userId = 1L;
        this.vehicle = vehicle;
        this.bookedFromDate = bookedFromDate;
        this.bookedToDate = bookedToDate;
        this.cityStart = cityStart;
        this.cityEnd = cityEnd;
        this.createDate = LocalDateTime.now();
    }
}
