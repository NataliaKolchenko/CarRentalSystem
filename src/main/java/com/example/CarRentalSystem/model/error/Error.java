package com.example.CarRentalSystem.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

@AllArgsConstructor
public class Error {
    private List<String> errorDescriptionList;


}
