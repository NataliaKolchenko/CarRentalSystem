package com.example.CarRentalSystem.exception;

public class BookingCannotBeCancelledException extends BusinessException{
    public BookingCannotBeCancelledException(String msg) {
        super(msg);
    }
}
