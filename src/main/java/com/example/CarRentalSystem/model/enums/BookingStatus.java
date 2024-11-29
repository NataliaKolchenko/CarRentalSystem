package com.example.CarRentalSystem.model.enums;

public enum BookingStatus {
    CREATED,
    WAITING_PAYMENT,
    PAYED,
    ACTIVE,
    CANCELLED,
    FINISHED;

    BookingStatus() {
    }
}
