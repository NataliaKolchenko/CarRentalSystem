package com.example.CarRentalSystem.exception;

public class BookingCannotBeFinishedException extends BusinessException{
    public BookingCannotBeFinishedException(String msg) {
        super(msg);
    }
}
