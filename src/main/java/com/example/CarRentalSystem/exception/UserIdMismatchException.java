package com.example.CarRentalSystem.exception;

public class UserIdMismatchException extends RuntimeException{
    public UserIdMismatchException(String msg) {
        super(msg);
    }
}
