package com.example.CarRentalSystem.service.interfaces;

import com.example.CarRentalSystem.model.SubType;

import java.util.List;

public interface SubTypeService {
    SubType create (SubType subTypeName);
    SubType update (Long subTypeId, String newSubTypeName);
    void deleteById(Long subTypeId);
    SubType getById(Long subTypeId);
    SubType getByName(String subTypeName);
    List<SubType> getAllSubTypes();
}
