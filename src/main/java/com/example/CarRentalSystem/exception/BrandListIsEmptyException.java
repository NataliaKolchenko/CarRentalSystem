package com.example.CarRentalSystem.exception;

public class BrandListIsEmptyException extends RuntimeException{
    public BrandListIsEmptyException(String msg){
        super(msg);
    }
}
