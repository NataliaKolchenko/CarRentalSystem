package com.example.CarRentalSystem.exception;

public class VehicleTypeAlreadyExistsException extends BusinessException{
    public VehicleTypeAlreadyExistsException(String msg) {
        super(msg);
    }
}
