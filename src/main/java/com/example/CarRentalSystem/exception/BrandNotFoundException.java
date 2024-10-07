package com.example.CarRentalSystem.exception;

public class BrandNotFoundException extends  RuntimeException{
    public BrandNotFoundException(String msg) {
        super(msg);
    }
}
