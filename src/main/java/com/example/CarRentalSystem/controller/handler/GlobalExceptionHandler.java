package com.example.CarRentalSystem.controller.handler;

import com.example.CarRentalSystem.exception.BusinessException;
import com.example.CarRentalSystem.exception.UserIdMismatchException;
import com.example.CarRentalSystem.exception.error.ErrorCarRentalSystem;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Validation Error Handler
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorCarRentalSystem> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Going through all validation errors and collecting messages
        List<String> stringList = ex.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ErrorCarRentalSystem(stringList), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BusinessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorCarRentalSystem> handleValidationExceptions(Exception ex) {
        return new ResponseEntity<>(new ErrorCarRentalSystem(List.of(ex.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorCarRentalSystem> handleValidationExceptions(ConstraintViolationException ex) {
        return new ResponseEntity<>(new ErrorCarRentalSystem(ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserIdMismatchException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleUserIdMismatchException(UserIdMismatchException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
