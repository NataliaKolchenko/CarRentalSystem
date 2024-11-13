package com.example.CarRentalSystem.controller.handler;

import com.example.CarRentalSystem.exception.BusinessException;
import com.example.CarRentalSystem.exception.error.Error;
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

    // Обработчик ошибок валидации
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Error> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Проход по всем ошибкам валидации и сбор сообщений
        List<String> stringList = ex.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new Error(stringList), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BusinessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Error> handleValidationExceptions(Exception ex) {
        return new ResponseEntity<>(new Error(List.of(ex.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Error> handleValidationExceptions(ConstraintViolationException ex) {
        return new ResponseEntity<>(new Error(ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList()),
                HttpStatus.BAD_REQUEST);
    }
}
