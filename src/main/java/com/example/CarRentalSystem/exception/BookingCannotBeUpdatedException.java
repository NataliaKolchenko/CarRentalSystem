package com.example.CarRentalSystem.exception;

public class BookingCannotBeUpdatedException extends BusinessException{
    public BookingCannotBeUpdatedException(String msg) {
        super(msg);
    }
}
