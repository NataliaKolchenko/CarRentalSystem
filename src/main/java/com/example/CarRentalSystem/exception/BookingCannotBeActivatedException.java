package com.example.CarRentalSystem.exception;

public class BookingCannotBeActivatedException extends BusinessException{
    public BookingCannotBeActivatedException(String msg) {
        super(msg);
    }
}
